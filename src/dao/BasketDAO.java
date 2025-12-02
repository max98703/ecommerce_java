package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Basket;
import models.Item;

public class BasketDAO extends BaseDAO {

    public int addToBasket(Basket b) throws Exception {
        int availableQty = 0;

        try (ResultSet rs = select("SELECT quantity FROM items WHERE id = ?", b.itemId)) {
            if (!rs.next()) return 0;          // Item doesn't exist
            availableQty = rs.getInt(1);
            if (b.quantity > availableQty) return 0; // Not enough stock
        }

        // Deduct quantity from items table
        update("UPDATE items SET quantity = quantity - ? WHERE id = ?", b.quantity, b.itemId);

        return insert("INSERT INTO basket (buyer_id, item_id, quantity) VALUES (?, ?, ?)", b.buyerId, b.itemId, b.quantity);
    }

    public List<Item> getBasketItems(int buyerId) throws Exception {
        List<Item> list = new ArrayList<>();
        String sql = """
                        SELECT b.id AS basket_id, i.*, b.quantity AS basket_quantity
                        FROM basket b
                        JOIN items i ON b.item_id = i.id
                        WHERE b.buyer_id = ?
                    """;

        try (ResultSet rs = select(sql, buyerId)) {
            while (rs.next()) {
                Item i = new Item();
                i.id = rs.getInt("id");
                i.sellerId = rs.getInt("seller_id");
                i.name = rs.getString("name");
                i.price = rs.getDouble("price");
                i.quantity = rs.getInt("basket_quantity");
                list.add(i);
            }
        }
        return list;
    }

    public int clearBasket(int buyerId) throws Exception {
        return update("DELETE FROM basket WHERE buyer_id=?", buyerId);
    }
}
