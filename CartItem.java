
/**
 * CartItem holds a lightweight copy of item data at time of adding to cart.
 */
public class CartItem {
    private final String name;
    private final Category category;
    private final double unitPrice;
    private int quantity;

    public CartItem(String name, Category category, double unitPrice, int quantity) {
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = Math.max(0, quantity);
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int q) {
        if (q < 0) return;
        this.quantity += q;
    }

    public double getSubTotal() {
        return unitPrice * quantity;
    }
}
