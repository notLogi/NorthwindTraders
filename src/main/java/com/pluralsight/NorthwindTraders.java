package com.pluralsight;

import java.sql.*;

public class NorthwindTraders {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println("Application needs a username and password to run.");
                System.exit(1);
            }

            String username = args[0];
            String password = args[1];

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);

            String query = """
                    SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                System.out.printf("""
                                Product ID: %s%n\
                                Name: %s%n\
                                Price: %.2f%n\
                                Stock: %d%n%n------------------
                                
                                """,
                        results.getString("ProductID"),
                        results.getString("ProductName"),
                        results.getDouble("UnitPrice"),
                        results.getInt("UnitsInStock"));
            }
            results.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
