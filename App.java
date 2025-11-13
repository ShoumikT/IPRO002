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
            System.out.println("5. Exit");
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
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }

        System.out.println("\nThank you for shopping with us!");
    }
}
