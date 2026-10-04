package webshop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Ger anslutningar till databasen. Den enda klassen som vet var databasen finns. */
public class DBManager {

    static {
        try {
            // I Tomcat registreras drivrutinen i WEB-INF/lib inte alltid automatiskt.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL-drivrutinen saknas", e);
        }
    }

    private DBManager() {
    }

    /** Öppnar en ny anslutning. Den som anropar måste stänga den. */
    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("webshop.db.url", "jdbc:mysql://localhost:3306/webshop");
        String user = System.getProperty("webshop.db.user", "webshop");
        String password = System.getProperty("webshop.db.password", "webshop");
        return DriverManager.getConnection(url, user, password);
    }
}
