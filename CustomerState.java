/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project5;


public abstract class CustomerState {
    
    protected Customer customer;
        
  
    public CustomerState(Customer customer){
        this.customer=customer;
    }
    
    public abstract void updateStatus();

    // Abstract method to redeem points
    public abstract void redeemPointsDiscount(int totalCost);
    
    
}
