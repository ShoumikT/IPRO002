
/**
 * FreshItem: subclass that adds shelf life attribute.
 */
public class FreshItem extends GroceryItem {
    private int shelfLifeDays;

    public FreshItem(String name, Category category, double price, int quantityInStock, int shelfLifeDays) {
        super(name, category, price, quantityInStock);
        this.shelfLifeDays = shelfLifeDays;
    }

    public int getShelfLifeDays() {
        return shelfLifeDays;
    }
}
