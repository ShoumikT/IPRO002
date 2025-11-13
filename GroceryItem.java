public class GroceryItem implements Sellable {
    protected int id;
    protected String name;
    protected double price;
    protected Category category;

    public GroceryItem(int id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public void displayInfo() {
        System.out.println(id + ". " + name + " - $" + price + " (" + category + ")");
    }

    @Override
    public double getCost() {
        return price;
    }
}
