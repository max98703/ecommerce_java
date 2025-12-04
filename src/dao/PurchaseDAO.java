package dao;

import models.Purchase;

public class PurchaseDAO extends BaseDAO {
    
    // Records a new purchase
    public int addPurchase(Purchase p) throws Exception {
        String sql = "INSERT INTO purchases (buyer_id, item_id, quantity, total_price) VALUES (?, ?, ?, ?)";
        return executeInsert(sql, p.buyerId, p.itemId, p.quantity, p.totalPrice);
    }
}
