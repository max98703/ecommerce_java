package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Basket;
import models.Item;

/**
 * DAO for managing buyer's basket operations.
 * 
 */
public class BasketDAO extends BaseDAO {

    // Adds an item to the buyer's basket, but only if there's enough in stock
    public int addToBasket(Basket b) throws Exception {
        int stockAvailable = 0;

        // Check stock availability
        try (ResultSet rs = runQuery("SELECT quantity FROM items WHERE id = ?", b.itemId).rs) {
            if (!rs.next()) {
                return 0;  // Item does not exit
            }
            stockAvailable = rs.getInt(1);
            if (b.quantity > stockAvailable) {
                return 0;  // Not enough stock
            }
        }

        // Reduce stock in items table
        executeUpdate("UPDATE items SET quantity = quantity - ? WHERE id = ?", 
               b.quantity, b.itemId);

        // Add to basket
        return executeInsert("INSERT INTO basket (buyer_id, item_id, quantity) VALUES (?, ?, ?)", 
                      b.buyerId, b.itemId, b.quantity);
    }

    // Retrieves all items in the buyer's basket with full item details
    public List<Item> getBasketItems(int buyerId) throws Exception {
        List<Item> basketContents = new ArrayList<>();

        // Join basket and items to get full details
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

    // Clears all items from the buyer's basket
    public int clearBasket(int buyerId) throws Exception {
        return executeUpdate("DELETE FROM basket WHERE buyer_id = ?", buyerId);
    }
}