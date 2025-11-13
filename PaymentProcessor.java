public class PaymentProcessor {
    public static void processPayment(double amount, PaymentMethod method) {
        System.out.println("\nProcessing payment of $" + amount + " using " + method + "...");
        System.out.println("Payment successful!");
    }
}
