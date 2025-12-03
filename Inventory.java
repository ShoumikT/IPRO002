
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Inventory manages items by name (unique names assumed).
 * Uses HashMap for quick lookup.
 */
public class Inventory {
    private final HashMap<String, GroceryItem> items = new HashMap<>();

    public void addItem(GroceryItem item) {
        if (item == null) return;
        items.put(item.getName(), item);
    }

    public boolean removeItem(String name) {
        if (name == null || !items.containsKey(name)) return false;
        items.remove(name);
        return true;
    }

    public GroceryItem getItem(String name) {
        if (name == null) return null;
        return items.get(name);
    }

    public ArrayList<GroceryItem> getAllItems() {
        return new ArrayList<GroceryItem>(items.values());
    }

    public ArrayList<GroceryItem> listByCategory(Category category) {
        ArrayList<GroceryItem> list = new ArrayList<>();
        for (GroceryItem gi : items.values()) {
            if (gi.getCategory() == category) {
                list.add(gi);
            }
        }
        return list;
    }
}
