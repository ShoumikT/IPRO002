import java.util.ArrayList;

public class Order {
    private ArrayList<GroceryItem> cart = new ArrayList<>();

    public void addItem(GroceryItem item) {
        cart.add(item);
    }

    public void displayOrder() {
        System.out.println("\nYour Order:");
        for (GroceryItem item : cart) {
            System.out.println("- " + item.getName() + " $" + item.getPrice());
        }
        System.out.println("Total: $" + getTotal());
    }

    public double getTotal() {
        double total = 0;
        for (GroceryItem item : cart) {
            total += item.getPrice();
        }
        return total;
    }

    public void clearOrder() {
        cart.clear();
    }
}
