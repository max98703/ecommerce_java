import Helper.InputHelper;
import dao.*;
import java.util.*;
import models.*;

// Main application class
public class Main {
    
    // Main application entry point
    public static void main(String[] args) {
        
        // Initialize DAOs and scanner
        Scanner sc = new Scanner(System.in);
        ItemDAO itemDAO = new ItemDAO();
        SellerDAO sellerDAO = new SellerDAO();
        BuyerDAO buyerDAO = new BuyerDAO();
        BasketDAO basketDAO = new BasketDAO();
        PurchaseDAO purchaseDAO = new PurchaseDAO();
        
        // Current logged-in buyer
        Buyer currentBuyer = null;
        
        // Main loop
        while (true) {

            // Display menu
            System.out.println("\n=== E-COMMERCE SYSTEM ===");
            System.out.println("1. Register Seller");
            System.out.println("2. Seller Add Item");
            System.out.println("3. Update Item Quantity");
            System.out.println("4. Register Buyer");
            System.out.println("5. Buyer Search Items");
            System.out.println("6. View Basket");
            System.out.println("7. Purchase Basket");
            System.out.println("8. Add Balance");
            System.out.println("9. Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // clear wrong input
                continue;
            }
            sc.nextLine(); // clear newline

            switch (choice) {

                case 1 -> { // Register Seller
                    Seller s = new Seller();
                    System.out.print("Name: "); s.name = sc.nextLine();
                    s.email = InputHelper.checkEmail(sc, "Email: ");
                    try {
                        int id = sellerDAO.addSeller(s);
                        System.out.println("Seller ID = " + id);
                    } catch (Exception e) {
                        System.out.println("Failed to add seller: " + e.getMessage());
                        return;
                    }
                }

                case 2 -> { // Seller Add Item
                    Item i = new Item();
                    i.sellerId = InputHelper.checkInt(sc, "Seller ID: ");
                    System.out.print("Item Name: ");i.name = sc.nextLine();
                    i.price = InputHelper.checkDouble(sc, "Price: ");
                    i.quantity = InputHelper.checkInt(sc, "Quantity: ");
                    try {
                        itemDAO.addItem(i);
                        System.out.println("Item added!");
                    } catch (Exception e) {
                        System.out.println("Failed to add item: " + e.getMessage());
                        return;
                    }
                }

                case 3 -> { // Update Item Quantity
                    System.out.print("Item ID: "); int id = sc.nextInt();
                    int q = InputHelper.checkInt(sc, "New Quantity:");
                    try {
                        int rows = itemDAO.updateItemQuantity(id, q);
                        if (rows == 0) {
                            System.out.println("No item found with ID = " + id);
                        } else {
                            System.out.println("Item updated!");
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to update item: " + e.getMessage());
                        return;
                    }
                }

                case 4 -> { // Register Buyer
                    Buyer b = new Buyer();
                    System.out.print("Name: "); b.name = sc.nextLine();
                    b.email = InputHelper.checkEmail(sc, "Email: ");
                    try {
                        int id = buyerDAO.addBuyer(b);
                        if (id != 0) {
                            String msg = (buyerDAO.isEmailExists(b.email)) ?
                                "Buyer with this email already exists. ID = " + id :
                                "Buyer registered! ID = " + id;

                            System.out.println(msg);
                        }

                        currentBuyer = b;
                        currentBuyer.id = id;
                        currentBuyer.balance = buyerDAO.fetchBalance(id);
                    } catch (Exception e) {
                        System.out.println("Failed to add buyer: " + e.getMessage());
                        return;
                    }
                }

                case 5 -> { // Buyer Search Items
                    if (currentBuyer == null) {
                        System.out.println("Please register/login as buyer first!");
                        break;
                    }
                    System.out.print("Search keyword: ");
                    String kw = sc.nextLine();
                    try {
                        List<Item> list = itemDAO.searchItems(kw);
                        if (list.isEmpty()) {
                            System.out.println("No items found.");
                            break;
                        }
                        for (Item it : list) {
                            System.out.println(it.id + ". " + it.name + " - $" + it.price + " (" + it.quantity + ")");
                        }
                        int itemId = InputHelper.checkInt(sc, "Add item to basket by ID (0 = skip): ");
                        if (itemId != 0) {
                            int qty = InputHelper.checkInt(sc, "Quantity:");
                            Basket b = new Basket();
                            b.buyerId = currentBuyer.id;
                            b.itemId = itemId;
                            b.quantity = qty;
                            int result = basketDAO.addToBasket(b);
                            if (result == 0) {
                                System.out.println("Failed to add to basket: item does not exist or quantity exceeds stock.");
                            } else {
                                System.out.println("Item added to basket!");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to search items: " + e.getMessage());
                        return;
                    }
                }

                case 6 -> { // View Basket
                    if (currentBuyer == null) {
                        System.out.println("Please register/login as buyer first!");
                        break;
                    }
                    try {
                        List<Item> basketItems = basketDAO.getBasketItems(currentBuyer.id);
                        if (basketItems.isEmpty()) {
                            System.out.println("Empty basket.");
                        } else {
                            double total = 0;

                            StringBuilder json = new StringBuilder();
                            json.append("[\n");

                            for (int i = 0; i < basketItems.size(); i++) {
                                Item it = basketItems.get(i);
                                total += it.price * it.quantity;

                                json.append("  {\n");
                                json.append("    \"id\": ").append(it.id).append(",\n");
                                json.append("    \"name\": \"").append(it.name).append("\",\n");
                                json.append("    \"price\": ").append(it.price).append(",\n");
                                json.append("    \"quantity\": ").append(it.quantity).append("\n");
                                json.append("  }");
                                if (i != basketItems.size() - 1) json.append(",");
                                json.append("\n");
                            }

                            json.append("]");

                            System.out.println(json.toString());
                            System.out.println("TOTAL = $" + total);
                        }
                    } catch (Exception e) {
                        System.out.println("Error fetching basket items: " + e.getMessage());
                        return;
                    }

                }

                case 7 -> { // Purchase Basket
                    if (currentBuyer == null) {
                        System.out.println("Please register/login as buyer first!");
                        break;
                    }
                    try {
                        List<Item> basketItems = basketDAO.getBasketItems(currentBuyer.id);
                        if (basketItems.isEmpty()) {
                            System.out.println("Basket is empty.");
                            break;
                        }

                        // Calculate total cost
                        double total = basketDAO.getBasketTotal(currentBuyer.id);

                        // Check buyer balance
                        if (currentBuyer.balance < total) {
                            System.out.println("Not enough balance!");
                            break;
                        }

                        for (Item it : basketItems) {
                            itemDAO.updateItemQuantity(it.id, it.quantity - it.quantity); // Deduct purchased quantity from stock
                            Purchase p = new Purchase();
                            p.buyerId = currentBuyer.id;
                            p.itemId = it.id;
                            p.quantity = it.quantity;
                            p.totalPrice = it.price * it.quantity;
                            purchaseDAO.addPurchase(p);
                        }

                        basketDAO.clearBasket(currentBuyer.id);
                        sellerDAO.updateSellerBalance(currentBuyer.id, total);
                        buyerDAO.updateBalance(currentBuyer.id, total);
                        currentBuyer.balance -= total;
                        System.out.println("Purchase successful! Total = $" + total);

                    } catch (Exception e) {
                        System.out.println("Error processing purchase: " + e.getMessage());
                        return;
                    }
                }
                case 8-> { // Add Balance
                    if (currentBuyer == null) {
                        System.out.println("Please register/login as buyer first!");
                        break;
                    }
                    double balance = InputHelper.checkDouble(sc, "Balace: ");
                    try {

                        buyerDAO.addBalance(currentBuyer.id, balance);
                        currentBuyer.balance += balance;
                        System.out.println("Successfully added balance!");

                    } catch (Exception e) {
                        System.out.println("Error processing purchase: " + e.getMessage());
                        return;
                    }
                }

                case 9 -> { System.out.println("Goodbye!"); return; }

                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
