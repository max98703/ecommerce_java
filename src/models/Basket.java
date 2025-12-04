package models;

// Basket model class
public class Basket {
    public int id;         // primary key
    public int buyerId;    // buyer who owns this basket
    public int itemId;     // item in basket
    public int quantity;   // quantity of item
}
