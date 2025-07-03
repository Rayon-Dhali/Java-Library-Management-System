/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project5;

/**
 *
 * @author vrajoo
 */
public class Gold extends CustomerState {
     public Gold(Customer customer) {
        super(customer);
    }

    /**
     * Downgrade to Silver if points drop below 1000
     */
    @Override
    public void updateStatus() {
        if (customer.getPoints() < 1000) {
            customer.setStatus(new Silver(customer));
        }
    }
    /**
     * Redeem as many points as possible (100 pts = $1 off)
     * Ensure final cost is not negative
     * Update points and downgrade status if needed
     */
    @Override
    public void redeemPointsDiscount(int totalCost) {
        int currentPoints = customer.getPoints();

        int redeemableDollars = currentPoints / 100;
        int discount = Math.min(redeemableDollars, totalCost);
        int finalCost = totalCost - discount;

        customer.setPoints(currentPoints - (discount * 100));

        System.out.println("Gold customer redeemed $" + discount + " from points.");
        System.out.println("Final cost after redemption: $" + finalCost);

        updateStatus(); // Check if points dropped below 1000
    }

    @Override
    public String toString() {
        return "Gold";
    }
}

