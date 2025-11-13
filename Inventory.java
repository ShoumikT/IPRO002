public class Inventory {
    private GroceryItem[] items = {
        new Fruit(1, "Apple", 1.50),
        new Fruit(2, "Banana", 0.90),
        new Vegetable(3, "Carrot", 2.00),
        new Vegetable(4, "Tomato", 2.50)
    };

    public void displayItems() {
        System.out.println("\nAvailable Groceries:");
        for (GroceryItem item : items) {
            item.displayInfo();
        }
    }

    public GroceryItem findItemById(int id) {
        for (GroceryItem item : items) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }
}
