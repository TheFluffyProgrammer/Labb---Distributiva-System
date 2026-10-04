package webshop.bo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import webshop.ui.CartItemDTO;
import webshop.ui.OrderDTO;
import webshop.ui.OrderLineDTO;

/**
 * Affärslagrets metoder för kundkorgen och ordrar.
 */
public class OrderHandler {

    private OrderHandler() {
    }

    /** Lägger en vara i korgen om det finns tillräckligt i lager. */
    public static void addToCart(ShoppingCart cart, int itemId, int quantity) throws ShopException {
        if (quantity < 1) {
            throw new ShopException("Antalet måste vara minst 1.");
        }
        try {
            Item item = Item.getItem(itemId);
            if (item == null) {
                throw new ShopException("Varan finns inte.");
            }
            if (item.getStock() == 0) {
                throw new ShopException(item.getName() + " är slut i lager.");
            }
            int wanted = cart.getQuantity(itemId) + quantity;
            if (wanted > item.getStock()) {
                throw new ShopException("Det finns bara " + item.getStock() + " st "
                        + item.getName() + " i lager.");
            }
            cart.add(item, quantity);
        } catch (SQLException e) {
            throw new ShopException("Kunde inte lägga varan i korgen", e);
        }
    }

    public static void removeFromCart(ShoppingCart cart, int itemId) {
        cart.remove(itemId);
    }

    public static List<CartItemDTO> getCartItems(ShoppingCart cart) {
        List<CartItemDTO> result = new ArrayList<>();
        for (CartItem ci : cart.getItems()) {
            Item item = ci.getItem();
            result.add(new CartItemDTO(item.getId(), item.getName(), item.getPrice(), ci.getQuantity()));
        }
        return result;
    }

    public static int getCartTotal(ShoppingCart cart) {
        return cart.getTotal();
    }

    /** Kunden skickar ordern från kundkorgen. Korgen töms om det gick bra. */
    public static int placeOrder(int userId, ShoppingCart cart) throws ShopException {
        if (cart.isEmpty()) {
            throw new ShopException("Kundkorgen är tom.");
        }
        try {
            int orderId = Order.placeOrder(userId, cart.getItems(), cart.getTotal());
            cart.clear();
            return orderId;
        } catch (SQLException e) {
            throw new ShopException("Ordern kunde inte skickas", e);
        }
    }

    public static List<OrderDTO> getOrdersForUser(int userId) throws ShopException {
        try {
            return toDTOs(Order.getOrdersForUser(userId));
        } catch (SQLException e) {
            throw new ShopException("Kunde inte hämta ordrarna", e);
        }
    }

    /** Ordrarna som lagerpersonalen ska skicka (status "Packas"). */
    public static List<OrderDTO> getOrdersToShip() throws ShopException {
        try {
            return toDTOs(Order.getOrdersToShip());
        } catch (SQLException e) {
            throw new ShopException("Kunde inte hämta ordrarna", e);
        }
    }

    /** Lagerpersonalen skickar ordern: status går från "Packas" till "Skickad" (transaktion, se DBOrder). */
    public static void shipOrder(int orderId) throws ShopException {
        try {
            Order.shipOrder(orderId);
        } catch (SQLException e) {
            throw new ShopException("Ordern kunde inte skickas", e);
        }
    }

    private static List<OrderDTO> toDTOs(List<Order> orders) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<OrderDTO> result = new ArrayList<>();
        for (Order order : orders) {
            List<OrderLineDTO> lines = new ArrayList<>();
            for (OrderLine line : order.getLines()) {
                lines.add(new OrderLineDTO(line.getItemName(), line.getQuantity(), line.getPrice()));
            }
            result.add(new OrderDTO(order.getId(), format.format(order.getCreated()),
                    order.getTotal(), order.getStatus(), lines));
        }
        return result;
    }
}
