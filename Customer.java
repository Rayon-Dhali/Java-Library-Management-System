package project5;

/**
 * Customer class that supports dynamic status switching using the State Design Pattern.
 * Status can be either Silver or Gold based on points.
 */
public class Customer {

    private final String username;
    private final String password;
    private int points;
    private CustomerState status; // This replaces the string "Silver"/"Gold"

    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;

        // Set initial status based on points
        if (points >= 1000) {
            this.status = new Gold(this);
        } else {
            this.status = new Silver(this);
        }
    }

    // === Getters ===

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public String getStatus() {
        return status.toString(); // Returns "Silver" or "Gold"
    }

    // === Setters ===

    public void setPoints(int points) {
        this.points = points;
        status.updateStatus(); // Ask current state to re-evaluate if change is needed
    }

    public void addPoints(int earned) {
        this.points += earned;
        status.updateStatus();
    }

    public void subtractPoints(int used) {
        this.points -= used;
        if (this.points < 0) this.points = 0;
        status.updateStatus();
    }

    public void setStatus(CustomerState status) {
        this.status = status;
    }

    // === State-driven methods ===

    public void redeemPoints(int totalCost) {
        status.redeemPointsDiscount(totalCost);  // Delegate to current status
    }
}
