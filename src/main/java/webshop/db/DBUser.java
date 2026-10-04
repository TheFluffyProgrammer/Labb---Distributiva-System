package webshop.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webshop.bo.User;

/** Databasdelen av en användare. */
public class DBUser extends User {

    private DBUser(int id, String username, String role, boolean active) {
        super(id, username, role, active);
    }

    /** Returnerar användaren om namn och lösenord stämmer, annars null. */
    public static User login(String username, String password) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createUser(rs);
                }
                return null;
            }
        }
    }

    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM users ORDER BY username");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(createUser(rs));
            }
        }
        return users;
    }

    public static boolean usernameExists(String username) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void addUser(String username, String password, String role) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();
        }
    }

    public static void updateRole(int id, String role) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE users SET role = ? WHERE id = ?")) {
            ps.setString(1, role);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public static void updateActive(int id, boolean active) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE users SET active = ? WHERE id = ?")) {
            ps.setBoolean(1, active);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private static User createUser(ResultSet rs) throws SQLException {
        return new DBUser(rs.getInt("id"), rs.getString("username"),
                rs.getString("role"), rs.getBoolean("active"));
    }
}
