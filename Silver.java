/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project5;

/**
 * Silver customer state: earns points, redeem points
 * upgrade to gold if points >= 1000
 * @author vrajoo
 */
public class Silver extends CustomerState {
    
    public Silver(Customer customer) {
        
        super(customer); // passes customer object to the superclass CustomerState, linking silver to a customer instance
        // Silver's state needs access to customer's points, to update the customer's state if gold status is reached
        
}

/** 
 * checking if the customer is able to upgrade to Gold 
 */
@Override 
public void updateStatus() { 
    
    if (customer.getPoints() >= 1000){ 
        
        customer.setStatus(new Gold(customer));
        } 

}

/**
 * Redeem points : for every 100 points, there is $1 deduction from Cost 
 * Ensures total cost does not go negative
 * Updates the customer's points and status
 * 
 */
@Override 
// when Redeem Points and Buy is pressed
public void redeemPointsDiscount(int totalCost) { 
    int currentPoints = customer.getPoints();
    
    int redeemableDollars = currentPoints / 100;
    int discount = Math.min(redeemableDollars, totalCost); // can't apply more discount than what the cost is  
    int finalCost = totalCost - discount; 

    customer.setPoints(currentPoints - (discount * 100)); // deducting the current points from the customers balance
    
    System.out.println("Silver customer redeemed $" + discount + "from points.");
    System.out.println("Final cost after redemption: $" + finalCost);

    updateStatus(); // to check if the status should change


}
@Override
public String toString() { 
    return "Silver";
}



}