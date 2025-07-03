package project5;

import javafx.scene.control.CheckBox;


public class Book {
    private final String title;
    private final double price;
    private CheckBox select; // Declare the select checkbox properly

    public Book(String bookTitle, double bookPrice) {
        this.title = bookTitle;
        this.price = bookPrice;
        this.select = new CheckBox(); // Initialize checkbox
    }

    public String getTitle() {
        return this.title;
    }

    public double getPrice() {
        return this.price;
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    void selectedProperty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
