package webshop.bo;

import java.sql.SQLException;
import java.util.List;

import webshop.db.DBUser;

/**
 * En användare. Rollen styr vad användaren får göra.
 */
public class User {

    /** Behörighetsklasser */
    public static final String CUSTOMER = "CUSTOMER";
    public static final String WAREHOUSE = "WAREHOUSE";
    public static final String ADMIN = "ADMIN";

    private final int id;
    private final String username;
    private final String role;
    private final boolean active;

    protected User(int id, String username, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.active = active;
    }

    static User login(String username, String password) throws SQLException {
        return DBUser.login(username, password);
    }

    static List<User> getAllUsers() throws SQLException {
        return DBUser.getAllUsers();
    }

    static boolean usernameExists(String username) throws SQLException {
        return DBUser.usernameExists(username);
    }

    static void addUser(String username, String password, String role) throws SQLException {
        DBUser.addUser(username, password, role);
    }

    static void updateRole(int id, String role) throws SQLException {
        DBUser.updateRole(id, role);
    }

    static void updateActive(int id, boolean active) throws SQLException {
        DBUser.updateActive(id, active);
    }

    static boolean isValidRole(String role) {
        return CUSTOMER.equals(role) || WAREHOUSE.equals(role) || ADMIN.equals(role);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }
}
