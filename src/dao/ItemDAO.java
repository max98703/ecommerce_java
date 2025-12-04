package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Item;

public class ItemDAO extends BaseDAO {

    // Check if seller exists
    public boolean sellerExists(int sellerId) throws Exception {
        String sql = "SELECT id FROM sellers WHERE id = ?";
        try (ResultSet rs = runQuery(sql, sellerId).rs) {
            return rs.next(); 
        }
    }

    // Add new item
    public int addItem(Item item) throws Exception {
        if (!sellerExists(item.sellerId)) {
            System.out.println("Cannot add item: Seller ID " + item.sellerId + " does not exist.");
            return 0;
        }
        
        // Insert item
        String sql = "INSERT INTO items (seller_id, name, price, quantity) VALUES (?, ?, ?, ?)";
        return executeInsert(sql, item.sellerId, item.name, item.price, item.quantity);
    }

    // Update item quantity
    public int updateItemQuantity(int itemId, int newQuantity) throws Exception {
        String sql = "UPDATE items SET quantity = ? WHERE id = ?";
        return executeUpdate(sql, newQuantity, itemId);
    }


    // Search items by name keyword
    public List<Item> searchItems(String keyword) throws Exception {
        String sql = "SELECT * FROM items WHERE name LIKE ?";
        List<Item> list = new ArrayList<>();

        try (ResultSet rs = runQuery(sql, "%" + keyword + "%").rs) {
            while (rs.next()) {
                Item i = new Item();
                i.id = rs.getInt("id");
                i.sellerId = rs.getInt("seller_id");
                i.name = rs.getString("name");
                i.price = rs.getDouble("price");
                i.quantity = rs.getInt("quantity");
                list.add(i);
            }
        }
        return list;
    }
}
