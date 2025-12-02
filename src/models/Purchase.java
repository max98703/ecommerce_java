package models;

import java.sql.Timestamp;

public class Purchase {
    public int id;           // primary key
    public int buyerId;      // buyer who purchased
    public int itemId;       // purchased item
    public int quantity;     // quantity purchased
    public double totalPrice;// total price
    public Timestamp purchaseDate; // purchase timestamp
}
