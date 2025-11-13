public class LoyaltySystem implements Rewardable {
    private double points = 0;

    @Override
    public void addPoints(double amountSpent) {
        points += amountSpent * 0.05; // 5% cashback
    }

    @Override
    public void displayPoints() {
        System.out.println("You earned loyalty points! Total points: " + points);
    }
}
