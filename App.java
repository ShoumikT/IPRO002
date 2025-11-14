public class App {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        LoyaltySystem loyaltySystem = new LoyaltySystem();
        Order order = new Order();

        System.out.println("=== Welcome to Local Fresh Groceries ===");

        boolean running = true;
        while (running) {
            System.out.println("\n1. View Groceries");
            System.out.println("2. Add Item to Order");
            System.out.println("3. View Order");
            System.out.println("4. Checkout");
            System.out.println("5. Remove Item from Order");
            System.out.println("6. Sort Order by Price");
            System.out.println("7. Filter Items by Category");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = In.nextInt();

            switch (choice) {
                case 1:
                    inventory.displayItems();
                    break;

                case 2:
                    System.out.print("Enter item ID to add: ");
                    int id = In.nextInt();
                    GroceryItem item = inventory.findItemById(id);
                    if (item != null) {
                        order.addItem(item);
                        System.out.println(item.getName() + " added to order.");
                    } else {
                        System.out.println("Invalid ID!");
                    }
                    break;

                case 3:
                    order.displayOrder();
                    break;

                case 4:
                    System.out.print("Enter payment method (1 = Cash, 2 = Card): ");
                    int payChoice = In.nextInt();
                    PaymentMethod method = (payChoice == 1) ? PaymentMethod.CASH : PaymentMethod.CARD;

                    double total = order.getTotal();
                    PaymentProcessor.processPayment(total, method);

                    loyaltySystem.addPoints(total);
                    loyaltySystem.displayPoints();

                    order.clearOrder();
                    break;

                    
                case 5:
                    System.out.println("Enter item ID to remove: ");
                    int removeId = In.nextInt();

                    GroceryItem removeItem = inventory.findItemById(removeId);

                    if (removeItem != null && order.containsItem(removeItem)) {
                        order.removeItem(removeItem);
                        System.out.println(removeItem.getName() + " removed from your order.");
                    } else {
                        System.out.println("Item not found in your order!");
                    }
                    break;
                case 6:
                     order.sortByPrice();
                    System.out.println("\nOrder sorted by price:");
                    order.displayOrder();
                    break;
                case 7:
                    System.out.println("\nFilter by Category:");
                    System.out.println("1 = Fruit");
                    System.out.println("2 = Vegetable");
                    System.out.print("Choose category: ");
                    int catChoice = In.nextInt();

                    Category selectedCategory;
                    if (catChoice == 1) {
                         selectedCategory = Category.FRUIT;
                    } else if (catChoice == 2) {
                         selectedCategory = Category.VEGETABLE;
                    } else {
                     System.out.println("Invalid category!");
                    break;
               }

                    GroceryItem[] filtered = inventory.filterByCategory(selectedCategory);

                    System.out.println("\nFiltered Items:");
                    for (GroceryItem gi : filtered) {
                    gi.displayInfo();
               }
                    break;
            
                    default:
                    System.out.println("Invalid choice!");
                    case 8:
                    running = false;
                    break;
               }
            }                    

                    System.out.println("\nThank you for shopping with us!");
    
    }
}
        
