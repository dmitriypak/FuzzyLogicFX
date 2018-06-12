package ru.bmstu.edu.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection==null){
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cvdata","postgres", "postgres");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return connection;
    }


}
