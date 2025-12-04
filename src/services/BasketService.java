package services;

import java.util.ArrayList;
import java.util.List;
import models.Item;

public class BasketService {
    private List<Item> items = new ArrayList<>();

    // Add item to basket
    public void add(Item i) {
        items.add(i);
    }

    // Get all items in basket
    public List<Item> getItems() {
        return items;
    }

    // Calculate total price of items in basket
    public double getTotal() {
        double total = 0;
        for (Item i : items) total += i.price;
        return total;
    }

    // Clear the basket
    public void clear() {
        items.clear();
    }
}
