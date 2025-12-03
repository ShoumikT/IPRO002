
/**
 * PackagedItem: subclass that adds brand.
 */
public class PackagedItem extends GroceryItem {
    private final String brand;

    public PackagedItem(String name, Category category, double price, int quantityInStock, String brand) {
        super(name, category, price, quantityInStock);
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
}
