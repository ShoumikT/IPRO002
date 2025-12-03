
/**
 * Simulated payment processor.
 */
public class PaymentProcessor {
    public static void processPayment(double amount, PaymentMethod method) {
        // simulate a delay or online processing in real life — here we just print
        System.out.println("Processing " + method.name() + " payment for $" + String.format("%.2f", amount));
        // In a real system we'd handle exceptions, network calls, etc.
    }
}
