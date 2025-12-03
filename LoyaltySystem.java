
/**
 * Loyalty rules: Points-based system.
 * - Earn: 1 point per $1 of actual paid amount.
 * - Redeem: 1 point = $0.01 (i.e., 100 points = $1.00)
 */
public class LoyaltySystem {
    private final double POINT_VALUE = 0.01; // 1 point = $0.01

    /**
     * Calculate earned points from paid amount.
     * 1 point per whole dollar.
     */
    public int calculateEarnedPoints(double paidAmount) {
        if (paidAmount <= 0) return 0;
        int pts = (int) Math.floor(paidAmount);
        return pts;
    }

    /**
     * Convert points -> dollars
     */
    public double pointsToDollars(int points) {
        if (points <= 0) return 0.0;
        return points * POINT_VALUE;
    }

    /**
     * Convert dollars -> points (rounded down)
     */
    public int dollarsToPoints(double dollars) {
        if (dollars <= 0) return 0;
        return (int) Math.floor(dollars / POINT_VALUE);
    }
}
