package ru.bmstu.edu.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection {
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn==null){
            try {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cvdata","postgres", "postgres");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return conn;
    }


}
