package net.holosen.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static String server = "localhost";
    private static String username = "postgres";
    private static String password = "123";
    private static String dbname = "schooldb";
    private static String port = "5432";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://" + server + ":" + port + "/" + dbname;
        return DriverManager.getConnection(url, username, password);
    }
}
