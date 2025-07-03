package project5;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;

public class Project5 extends Application {
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<Book> books = FXCollections.observableArrayList();
    private Customer currentCustomer;

    @Override
    public void start(Stage primaryStage) {
        loadCustomersFromFile();
        loadBooksFromFile();

        Text wlcmeText = new Text("Welcome to the BookStore App");

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginbtn = new Button("Login");
        loginbtn.setOnAction(event -> {
            String enteredUsername = userField.getText();
            String enteredPassword = passField.getText();

            // Clear and reload lists BEFORE login check
            customerList.clear();
            loadCustomersFromFile();
            books.clear();
            loadBooksFromFile();

            for (Customer c : customerList) {
                if (enteredUsername.equals(c.getUsername()) && enteredPassword.equals(c.getPassword())) {
                    currentCustomer = c;
                    CustomerStartScreen(primaryStage);
                    return;
                }
            }

            if (enteredUsername.equals("admin") && enteredPassword.equals("admin")) {
                OwnerStartScreen(primaryStage);
            } else {
                System.out.println("Login Failed! Incorrect username or password.");
            }
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(wlcmeText, userLabel, userField, passLabel, passField, loginbtn);

        Scene scene = new Scene(vbox, 600, 600);

        primaryStage.setTitle("BookStore App");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            saveCustomersToFile();
            saveBooksToFile();
        });
    }

    private void saveCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer c : customerList) {
                writer.write(c.getUsername() + "," + c.getPassword() + "," + c.getPoints());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomersFromFile() {
        File file = new File("customers.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    int points = Integer.parseInt(parts[2]);
                    customerList.add(new Customer(username, password, points));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book b : books) {
                writer.write(b.getTitle() + "," + b.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksFromFile() {
        File file = new File("books.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String title = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    books.add(new Book(title, price));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OwnerStartScreen(Stage primaryStage) {
        Text ownerText = new Text("Owner Start Screen");
        Button booksBtn = new Button("Books");
        Button customersBtn = new Button("Customers");
        Button logoutBtn = new Button("Logout");

        logoutBtn.setOnAction(event -> start(primaryStage));
        booksBtn.setOnAction(event -> OwnerBookScreen(primaryStage));
        customersBtn.setOnAction(event -> OwnerCustomerScreen(primaryStage));

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(ownerText, booksBtn, customersBtn, logoutBtn);

        Scene ownerScene = new Scene(vbox, 600, 600);
        primaryStage.setScene(ownerScene);
        primaryStage.show();
    }

    public void OwnerBookScreen(Stage primaryStage) {
        Text ownerText = new Text("Owner Book Screen");
        Label bookTitleLabel = new Label("Title:");
        TextField bookTitleField = new TextField();
        Label bookPriceLabel = new Label("Price:");
        TextField priceField = new TextField();

        TableView<Book> table = new TableView<>();
        TableColumn<Book, String> bookNameCol = new TableColumn<>("Book Name");
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, Double> bookPriceCol = new TableColumn<>("Book Price");
        bookPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        bookNameCol.setMinWidth(200);
        bookPriceCol.setMinWidth(100);

        table.getColumns().addAll(bookNameCol, bookPriceCol);
        table.setItems(books);

        Label label = new Label("Book List");
        Button addBookBtn = new Button("Add Book");
        Button deleteBookBtn = new Button("Delete Book");
        Button returnBack = new Button("Return to Owner Start Screen");

        addBookBtn.setOnAction(event -> {
            try {
                String title = bookTitleField.getText();
                double price = Double.parseDouble(priceField.getText());

                boolean bookExists = books.stream().anyMatch(book -> book.getTitle().equals(title) && book.getPrice() == price);

                if (bookExists) {
                    System.out.println("The book with the same title and price already exists.");
                } else {
                    books.add(new Book(title, price));
                    System.out.println("Book added successfully.");
                }

                bookTitleField.clear();
                priceField.clear();
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Please enter a valid number.");
            }
        });

        deleteBookBtn.setOnAction(event -> {
            Book selectedBook = table.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                books.remove(selectedBook);
            } else {
                System.out.println("No book selected to delete.");
            }
        });

        returnBack.setOnAction(event -> OwnerStartScreen(primaryStage));

        VBox vbox = new VBox(10);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(ownerText, label, table, bookTitleLabel, bookTitleField, bookPriceLabel, priceField, addBookBtn, deleteBookBtn, returnBack);

        Scene scene = new Scene(new Group(), 600, 800);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Owner Book Screen");
        primaryStage.show();
    }

    public void OwnerCustomerScreen(Stage primaryStage) {
        Text ownerText = new Text("Owner Customer Screen");
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        Label pointsLabel = new Label("Points:");
        TextField pointsField = new TextField();

        TableView<Customer> table = new TableView<>();
        TableColumn<Customer, String> usernameCol = new TableColumn<>("Customer Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Customer, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        usernameCol.setMinWidth(200);
        passwordCol.setMinWidth(100);
        pointsCol.setMinWidth(100);

        table.getColumns().addAll(usernameCol, passwordCol, pointsCol);
        table.setItems(customerList);

        Label label = new Label("Customer List");
        Button addCustomerBtn = new Button("Add Customer");
        Button deleteCustomerBtn = new Button("Delete Customer");
        Button returnBack = new Button("Return to Owner Start Screen");

        addCustomerBtn.setOnAction(event -> {
            try {
                String username = usernameField.getText();
                String password = passwordField.getText();
                int points = Integer.parseInt(pointsField.getText());
                Customer newCustomer = new Customer(username, password, points);
                customerList.add(newCustomer);
                usernameField.clear();
                passwordField.clear();
                pointsField.clear();
            } catch (NumberFormatException e) {
                System.out.println("Invalid points. Please enter a valid number.");
            }
        });

        deleteCustomerBtn.setOnAction(event -> {
            Customer selectedCustomer = table.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                customerList.remove(selectedCustomer);
            } else {
                System.out.println("No customer selected to delete.");
            }
        });

        returnBack.setOnAction(event -> OwnerStartScreen(primaryStage));

        VBox vbox = new VBox(10);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(ownerText, label, table, usernameLabel, usernameField, passwordLabel, passwordField, pointsLabel, pointsField, addCustomerBtn, deleteCustomerBtn, returnBack);

        Scene scene = new Scene(new Group(), 600, 800);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Owner Customer Screen");
        primaryStage.show();
    }

    public void CustomerStartScreen(Stage primaryStage) {
        Text screenTitle = new Text("Customer Start Screen");
        Label welcomeLabel = new Label("Welcome " + currentCustomer.getUsername() +
                ". You have " + currentCustomer.getPoints() + " points. Your status is " + currentCustomer.getStatus() + ".");

        books.removeIf(book -> book.getSelect().isSelected());

        Button buyBtn = new Button("Buy");
        buyBtn.setOnAction(event -> CustomerCostScreen(primaryStage));

        Button redeemBuyBtn = new Button("Redeem Points & Buy");
        redeemBuyBtn.setOnAction(event -> CustomerCostScreen(primaryStage));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(event -> start(primaryStage));

        TableView<Book> table = new TableView<>();
        table.setItems(books);

        TableColumn<Book, String> nameCol = new TableColumn<>("Book Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, Double> priceCol = new TableColumn<>("Book Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Book, CheckBox> selectCol = new TableColumn<>("Select");
        selectCol.setCellValueFactory(new PropertyValueFactory<>("select"));

        nameCol.setMinWidth(200);
        priceCol.setMinWidth(100);
        selectCol.setMinWidth(100);

        table.getColumns().addAll(nameCol, priceCol, selectCol);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(screenTitle, welcomeLabel, table, buyBtn, redeemBuyBtn, logoutBtn);

        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Start Screen");
        primaryStage.show();
    }

    public void CustomerCostScreen(Stage primaryStage) {
        Text screenTitle = new Text("Customer Cost Screen");

        double totalCost = 0;
        ObservableList<Book> selectedBooks = FXCollections.observableArrayList();

        for (Book book : books) {
            if (book.getSelect().isSelected()) {
                totalCost += book.getPrice();
                selectedBooks.add(book);
            }
        }

        int redeemableDollars = currentCustomer.getPoints() / 100;
        int discount = (int) Math.min(redeemableDollars, totalCost);
        double finalCost = totalCost - discount;

        int pointsUsed = discount * 100;
        currentCustomer.setPoints(currentCustomer.getPoints() - pointsUsed);

        int earnedPoints = (int) (finalCost * 10);
        currentCustomer.setPoints(currentCustomer.getPoints() + earnedPoints);
        books.removeAll(selectedBooks);

        saveCustomersToFile();
        saveBooksToFile();

        Label costLabel = new Label("Total Cost: " + totalCost + "\n" +
                "Discount Applied: " + discount + "\n" +
                "Final Cost: " + finalCost + "\n" +
                "Points Earned: " + earnedPoints + "\n" +
                "Total Points: " + currentCustomer.getPoints() + "\n" +
                "Status: " + currentCustomer.getStatus());

        Button backBtn = new Button("Back to Start Screen");
        backBtn.setOnAction(event -> CustomerStartScreen(primaryStage));

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(screenTitle, costLabel, backBtn);

        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Cost Screen");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}