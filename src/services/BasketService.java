package services;

import models.Item;
import java.util.ArrayList;
import java.util.List;

public class BasketService {
    private List<Item> items = new ArrayList<>();

    // Add item to basket
    public void add(Item i) {
        items.add(i);
    }

    // Get all items
    public List<Item> getItems() {
        return items;
    }

    // Calculate total price
    public double getTotal() {
        double total = 0;
        for (Item i : items) total += i.price;
        return total;
    }

    // Clear basket after purchase
    public void clear() {
        items.clear();
    }
}
