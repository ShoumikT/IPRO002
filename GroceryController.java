
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Controller: mediates between Model and View, handles user actions.
 */
public class GroceryController {
    private final GroceryModel model;

    public GroceryController(GroceryModel model) {
        this.model = model;
    }

    /* Inventory operations */

    public ArrayList<GroceryItem> listAllItems() {
        return model.getInventory().getAllItems();
    }

    public ArrayList<GroceryItem> listByCategory(Category category) {
        return model.getInventory().listByCategory(category);
    }

    public boolean addNewItemToInventory(GroceryItem item) {
        if (item == null) return false;
        model.getInventory().addItem(item);
        return true;
    }

    public boolean removeItemFromInventory(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return model.getInventory().removeItem(name);
    }

    public GroceryItem getItemByName(String name) {
        return model.getInventory().getItem(name);
    }

    /* Cart operations */

    public boolean addToCart(String name, int quantity) {
        if (name == null || quantity < 1) return false;
        GroceryItem base = model.getInventory().getItem(name);
        if (base == null) return false;
        if (base.getQuantityInStock() < quantity) return false;

        // create a cart item copy (so inventory and cart manage quantity separately)
        CartItem ci = new CartItem(base.getName(), base.getCategory(), base.getPrice(), quantity);
        model.getCart().addItem(ci);

        // reduce stock in inventory
        base.reduceStock(quantity);

        // update model totals
        model.updateCartTotal();

        return true;
    }

    public boolean removeFromCart(String name) {
        if (name == null) return false;
        CartItem removed = model.getCart().removeItem(name);
        if (removed != null) {
            // return quantity to stock
            GroceryItem base = model.getInventory().getItem(name);
            if (base != null) {
                base.increaseStock(removed.getQuantity());
            }
            model.updateCartTotal();
            return true;
        }
        return false;
    }

    public ArrayList<CartItem> viewCartItems() {
        return model.getCart().getItems();
    }

    /* Checkout & Payment */

    /**
     * Pay for cart using selected method.
     * @param method PaymentMethod enum
     * @param usePointsToCoverAmount points to attempt to use from customer (0 to available)
     * @return receipt string summary
     */
    public String checkout(PaymentMethod method, int usePointsToCoverAmount) {
        double total = model.getCart().getTotal();
        Customer cust = model.getCustomer();
        LoyaltySystem loyalty = model.getLoyaltySystem();
        int pointsBefore = cust.getPoints();

        // Calculate points redemption (1 point = $0.01) by default
        double redeemedValue = 0.0;
        int redeemedPoints = 0;
        if (usePointsToCoverAmount > 0) {
            if (usePointsToCoverAmount > cust.getPoints()) {
                usePointsToCoverAmount = cust.getPoints();
            }
            redeemedPoints = usePointsToCoverAmount;
            redeemedValue = loyalty.pointsToDollars(redeemedPoints);
            // cap redeemedValue to total
            if (redeemedValue > total) {
                redeemedValue = total;
                redeemedPoints = loyalty.dollarsToPoints(total);
            }
            total = total - redeemedValue;
            cust.redeemPoints(redeemedPoints);
        }

        // Payment processing (simulated)
        PaymentProcessor.processPayment(total, method);

        // Earn new points for remaining paid amount (1 point per $1)
        int earned = model.getLoyaltySystem().calculateEarnedPoints(total);
        cust.addPoints(earned);

        // Build receipt
        StringBuilder receipt = new StringBuilder();
        receipt.append("RECEIPT\n");
        for (CartItem ci : model.getCart().getItems()) {
            receipt.append(String.format("%s x%d - $%.2f\n", ci.getName(), ci.getQuantity(), ci.getSubTotal()));
        }
        receipt.append(String.format("Subtotal: $%.2f\n", model.getCart().getSubTotalBeforeRedemption(redeemedValue)));
        if (redeemedPoints > 0) {
            receipt.append(String.format("Redeemed points: %d -> $%.2f\n", redeemedPoints, redeemedValue));
        }
        receipt.append(String.format("Paid: $%.2f via %s\n", total, method.name()));
        receipt.append(String.format("Points earned: %d  (Before: %d, After: %d)\n", earned, pointsBefore, cust.getPoints()));

        // Clear cart
        model.getCart().clear();
        model.updateCartTotal();

        return receipt.toString();
    }

    /* Loyalty & Customer */

    public int getCustomerPoints() {
        return model.getCustomer().getPoints();
    }

    public void addCustomerPoints(int p) {
        model.getCustomer().addPoints(p);
    }

    public boolean redeemCustomerPoints(int points) {
        return model.getCustomer().redeemPoints(points);
    }

    /* Admin helpers */

    public void restockItem(String name, int qty) {
        GroceryItem item = model.getInventory().getItem(name);
        if (item != null && qty > 0) {
            item.increaseStock(qty);
        }
    }

    public void setCustomerName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            model.getCustomer().setName(name);
        }
    }

    public String getCustomerName() {
        return model.getCustomer().getName();
    }
}
