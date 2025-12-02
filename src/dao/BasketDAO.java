package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Basket;
import models.Item;

/**
 * Handles everything related to the shopping basket.
 * Lets buyers add items, view their basket, and clear it when done.
 */
public class BasketDAO extends BaseDAO {

    // Adds an item to the buyer's basket, but only if there's enough in stock
    public int addToBasket(Basket b) throws Exception {
        int stockAvailable = 0;

        // First, check if the item exists and how many are in stock
        try (ResultSet rs = runQuery("SELECT quantity FROM items WHERE id = ?", b.itemId).rs) {
            if (!rs.next()) {
                return 0;  // Item not found – can't add to basket
            }
            stockAvailable = rs.getInt(1);
            if (b.quantity > stockAvailable) {
                return 0;  // Not enough stock to fulfill request
            }
        }

        // Reserve the items by reducing stock
        executeUpdate("UPDATE items SET quantity = quantity - ? WHERE id = ?", 
               b.quantity, b.itemId);

        // Now actually add to the basket table
        return executeInsert("INSERT INTO basket (buyer_id, item_id, quantity) VALUES (?, ?, ?)", 
                      b.buyerId, b.itemId, b.quantity);
    }

    // Fetches all items currently in the buyer's basket
    public List<Item> getBasketItems(int buyerId) throws Exception {
        List<Item> basketContents = new ArrayList<>();

        // We join basket with items to get full details
        String sql = """
                     SELECT b.id AS basket_id, i.*, b.quantity AS basket_quantity
                     FROM basket b
                     JOIN items i ON b.item_id = i.id
                     WHERE b.buyer_id = ?
                     """;

        try (ResultSet rs = runQuery(sql, buyerId).rs) {
            while (rs.next()) {
                Item item = new Item();
                item.id = rs.getInt("id");
                item.sellerId = rs.getInt("seller_id");
                item.name = rs.getString("name");
                item.price = rs.getDouble("price");
                item.quantity = rs.getInt("basket_quantity"); // quantity in basket, not total stock
                basketContents.add(item);
            }
        }
        return basketContents;
    }

    // Clears the buyer's entire basket (useful after purchase or when starting fresh)
    public int clearBasket(int buyerId) throws Exception {
        return executeUpdate("DELETE FROM basket WHERE buyer_id = ?", buyerId);
    }
}