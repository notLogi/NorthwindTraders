package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class NorthwindTraders {
    public static void main(String[] args) throws ClassNotFoundException {
        if (args.length != 2) {
            System.out.println("Application needs a username and password to run.");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);
                while (true) {
                    System.out.println("What do you want to do?");
                    System.out.println("""
                            1) Display all products
                            2) Display all customers
                            0) Exit
                            """);
                    Scanner scanner = new Scanner(System.in);
                    int input = scanner.nextInt();
                    if (input == 0) return;
                    String query;
                    switch (input) {
                        case 1 -> query = """
                                SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products;
                                """;
                        case 2 -> query = """
                                SELECT ContactName, CompanyName, City, Country, Phone FROM customers
                                ORDER BY Country;
                                """;
                        default -> {
                            System.out.println("Please enter a valid input");
                            continue;
                        }
                    }

                    preparedStatement = connection.prepareStatement(query);

                    results = preparedStatement.executeQuery();

                    if (input == 1) {
                        while (results.next()) {
                            System.out.printf("""
                                            Product ID: %d%n\
                                            Name: %s%n\
                                            Price: %.2f%n\
                                            Stock: %d%n%n------------------
                                            
                                            """,
                                    results.getInt("ProductID"),
                                    results.getString("ProductName"),
                                    results.getDouble("UnitPrice"),
                                    results.getInt("UnitsInStock"));
                        }
                    } else {
                        while (results.next()) {
                            System.out.printf("""
                                            Contact Name: %s%n\
                                            Company Name: %s%n\
                                            City: %s%n\
                                            Country: %s%n\
                                            Phone: %s%n%n------------------
                                            """,
                                    results.getString("ContactName"),
                                    results.getString("CompanyName"),
                                    results.getString("City"),
                                    results.getString("Country"),
                                    results.getString("Phone"));
                        }
                    }
                }
            }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Enter a number please");
        }
        finally {
            if (results != null) {
                try {
                    results.close();
                    System.err.println("Closed result");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    System.err.println("Closed statement");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                    System.err.println("Closed connection");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
