package webshop.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import webshop.bo.CartItem;
import webshop.bo.Order;
import webshop.bo.OrderLine;
import webshop.bo.ShopException;

/** Databasdelen av en order. Både att lägga och att skicka en order görs i en transaktion. */
public class DBOrder extends Order {

    private DBOrder(int id, Timestamp created, int total, String status, List<OrderLine> lines) {
        super(id, created, total, status, lines);
    }

    /** Sparar ordern i en transaktion. Rollback om en vara saknas i lager. */
    public static int placeOrder(int userId, List<CartItem> cartItems, int total)
            throws SQLException, ShopException {
        try (Connection con = DBManager.getConnection()) {
            con.setAutoCommit(false); // transaktionen börjar
            try {
                int orderId = insertOrder(con, userId, total);
                for (CartItem ci : cartItems) {
                    if (!decreaseStock(con, ci.getItem().getId(), ci.getQuantity())) {
                        throw new ShopException("Det finns inte tillräckligt av "
                                + ci.getItem().getName() + " i lager.");
                    }
                    insertOrderLine(con, orderId, ci);
                }
                con.commit(); // allt gick bra, spara
                return orderId;
            } catch (SQLException | ShopException e) {
                con.rollback(); // något gick fel, ångra allt
                throw e;
            }
        }
    }

    private static int insertOrder(Connection con, int userId, int total) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO orders (user_id, total, status) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, total);
            ps.setString(3, PACKING); // en ny order ska packas av lagerpersonalen
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    /** Minskar lagret bara om det finns tillräckligt. Returnerar false annars. */
    private static boolean decreaseStock(Connection con, int itemId, int quantity) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE items SET stock = stock - ? WHERE id = ? AND stock >= ?")) {
            ps.setInt(1, quantity);
            ps.setInt(2, itemId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() == 1;
        }
    }

    private static void insertOrderLine(Connection con, int orderId, CartItem ci) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO order_items (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, orderId);
            ps.setInt(2, ci.getItem().getId());
            ps.setInt(3, ci.getQuantity());
            ps.setInt(4, ci.getItem().getPrice());
            ps.executeUpdate();
        }
    }

    /** Skickar ordern i en transaktion: status Packas blir Skickad. */
    public static void shipOrder(int orderId) throws SQLException, ShopException {
        try (Connection con = DBManager.getConnection()) {
            con.setAutoCommit(false); // transaktionen börjar
            try {
                if (!markAsShipped(con, orderId)) {
                    throw new ShopException("Ordern finns inte eller är redan skickad.");
                }
                con.commit(); // allt gick bra, spara
            } catch (SQLException | ShopException e) {
                con.rollback(); // något gick fel, ångra allt
                throw e;
            }
        }
    }

    /** Ändrar statusen bara om ordern fortfarande packas. Returnerar false annars. */
    private static boolean markAsShipped(Connection con, int orderId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE orders SET status = ? WHERE id = ? AND status = ?")) {
            ps.setString(1, SHIPPED);
            ps.setInt(2, orderId);
            ps.setString(3, PACKING);
            return ps.executeUpdate() == 1;
        }
    }

    public static List<Order> getOrdersForUser(int userId) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM orders WHERE user_id = ? ORDER BY id DESC")) {
            ps.setInt(1, userId);
            return readOrders(con, ps);
        }
    }

    /** Alla ordrar som väntar på att skickas, äldst först. */
    public static List<Order> getOrdersToShip() throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM orders WHERE status = ? ORDER BY id")) {
            ps.setString(1, PACKING);
            return readOrders(con, ps);
        }
    }

    private static List<Order> readOrders(Connection con, PreparedStatement ps) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                orders.add(new DBOrder(id, rs.getTimestamp("created"), rs.getInt("total"),
                        rs.getString("status"), getOrderLines(con, id)));
            }
        }
        return orders;
    }

    private static List<OrderLine> getOrderLines(Connection con, int orderId) throws SQLException {
        List<OrderLine> lines = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT i.name, oi.quantity, oi.price FROM order_items oi "
                        + "JOIN items i ON i.id = oi.item_id WHERE oi.order_id = ?")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lines.add(new OrderLine(rs.getString("name"), rs.getInt("quantity"), rs.getInt("price")));
                }
            }
        }
        return lines;
    }
}
