
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model: holds Inventory, ShoppingCart and Customer data.
 * Uses JavaFX properties where UI binding is helpful.
 */
public class GroceryModel {
    private final Inventory inventory;
    private final ShoppingCart cart;
    private final Customer customer;
    private final LoyaltySystem loyaltySystem;

    // Properties for UI binding (total)
    private final SimpleDoubleProperty cartTotal = new SimpleDoubleProperty(0);

    public GroceryModel() {
        this.inventory = new Inventory();
        this.cart = new ShoppingCart();
        this.customer = new Customer("Guest");
        this.loyaltySystem = new LoyaltySystem();

        // Example initial items
        inventory.addItem(new FreshItem("Apple", Category.FRUIT, 1.50, 50, 7));
        inventory.addItem(new FreshItem("Banana", Category.FRUIT, 0.90, 60, 5));
        inventory.addItem(new FreshItem("Carrot", Category.VEGETABLE, 2.00, 40, 10));
        inventory.addItem(new PackagedItem("Pasta", Category.OTHER, 3.50, 20, "BrandA"));
        inventory.addItem(new PackagedItem("Milk", Category.DAIRY, 2.80, 30, "DairyCo"));

        // update cart total when cart changes
        // not using complex listeners to keep Week 1-9 concepts; we'll call updateCartTotal() from controller on changes
        updateCartTotal();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LoyaltySystem getLoyaltySystem() {
        return loyaltySystem;
    }

    public double getCartTotal() {
        return cartTotal.get();
    }

    public SimpleDoubleProperty cartTotalProperty() {
        return cartTotal;
    }

    public void updateCartTotal() {
        double total = cart.getTotal();
        cartTotal.set(total);
    }
}
