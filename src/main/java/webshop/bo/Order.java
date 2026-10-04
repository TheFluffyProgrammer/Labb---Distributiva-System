package webshop.bo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import webshop.db.DBOrder;

/** En order med sina orderrader och status (Packas eller Skickad). */
public class Order {

    public static final String PACKING = "Packas";
    public static final String SHIPPED = "Skickad";

    private final int id;
    private final Date created;
    private final int total;
    private final String status;
    private final List<OrderLine> lines;

    protected Order(int id, Date created, int total, String status, List<OrderLine> lines) {
        this.id = id;
        this.created = created;
        this.total = total;
        this.status = status;
        this.lines = lines;
    }

    static int placeOrder(int userId, List<CartItem> items, int total) throws SQLException, ShopException {
        return DBOrder.placeOrder(userId, items, total);
    }

    static List<Order> getOrdersForUser(int userId) throws SQLException {
        return DBOrder.getOrdersForUser(userId);
    }

    /** Alla ordrar som väntar på att skickas (status "Packas"). */
    static List<Order> getOrdersToShip() throws SQLException {
        return DBOrder.getOrdersToShip();
    }

    static void shipOrder(int orderId) throws SQLException, ShopException {
        DBOrder.shipOrder(orderId);
    }

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public int getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderLine> getLines() {
        return lines;
    }
}
