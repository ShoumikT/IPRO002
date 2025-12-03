
import java.util.ArrayList;

/**
 * ShoppingCart holds CartItem objects. It does NOT persist stock, stock is handled by Inventory.
 */
public class ShoppingCart {
    private final ArrayList<CartItem> items = new ArrayList<>();

    public void addItem(CartItem ci) {
        if (ci == null) return;
        // if same item exists, increment quantity
        for (CartItem i : items) {
            if (i.getName().equals(ci.getName())) {
                i.addQuantity(ci.getQuantity());
                return;
            }
        }
        items.add(ci);
    }

    public CartItem removeItem(String name) {
        if (name == null) return null;
        for (int i = 0; i < items.size(); i++) {
            CartItem ci = items.get(i);
            if (ci.getName().equals(name)) {
                items.remove(i);
                return ci;
            }
        }
        return null;
    }

    public ArrayList<CartItem> getItems() {
        return new ArrayList<CartItem>(items);
    }

    public double getTotal() {
        double t = 0;
        for (CartItem ci : items) {
            t += ci.getSubTotal();
        }
        return t;
    }

    public double getSubTotalBeforeRedemption(double redeemedValue) {
        double t = getTotal();
        return t + redeemedValue;
    }

    public void clear() {
        items.clear();
    }
}
