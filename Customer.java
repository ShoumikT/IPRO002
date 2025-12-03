
/**
 * Simple customer with name and points.
 */
public class Customer {
    private String name;
    private int points; // integer points

    public Customer(String name) {
        this.name = (name == null ? "Guest" : name);
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        this.name = name;
    }

    public void addPoints(int p) {
        if (p <= 0) return;
        this.points += p;
    }

    public boolean redeemPoints(int p) {
        if (p <= 0) return false;
        if (p > points) return false;
        points -= p;
        return true;
    }
}
