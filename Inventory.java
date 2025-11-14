public class Inventory {
    private GroceryItem[] items = {
        new Fruit( 1,  "Apple", 1.50),
        new Fruit(2, "Banana", 0.90),
        new Vegetable(3, "Carrot", 2.00),
        new Vegetable(4, "Tomato", 2.50),
        new Vegetable(5, "Spinach", 3.00)
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

public GroceryItem[] filterByCategory(Category c) {
        int count = 0;
        for (GroceryItem item : items) {
            if (item.category == c) {
                count++;
            }
        }

        GroceryItem[] result = new GroceryItem[count];
        int index = 0;
        for (GroceryItem item : items) {
            if (item.category == c) {
                result[index] = item;
                index++;
            }
        }

        return result;
    }

    public void displayFiltered(GroceryItem[] filtered) {
        for (GroceryItem item : filtered) {
            item.displayInfo();
        }
    }
}

