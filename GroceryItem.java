
/**
 * Parent class for grocery items.
 */
public class GroceryItem {
    private final String name;
    private final Category category;
    private final double price;
    private int quantityInStock;

    public GroceryItem(String name, Category category, double price, int quantityInStock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantityInStock = Math.max(0, quantityInStock);
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void reduceStock(int amount) {
        if (amount < 0) return;
        this.quantityInStock = Math.max(0, this.quantityInStock - amount);
    }

    public void increaseStock(int amount) {
        if (amount < 0) return;
        this.quantityInStock += amount;
    }

    @Override
    public String toString() {
        return name + " | " + category.name() + " | $" + String.format("%.2f", price) + " | qty:" + quantityInStock;
    }
}
