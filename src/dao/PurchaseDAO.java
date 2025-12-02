package dao;

import models.Purchase;

public class PurchaseDAO extends BaseDAO {

    public int addPurchase(Purchase p) throws Exception {
        String sql = "INSERT INTO purchases (buyer_id, item_id, quantity, total_price) VALUES (?, ?, ?, ?)";
        return insert(sql, p.buyerId, p.itemId, p.quantity, p.totalPrice);
    }
}
