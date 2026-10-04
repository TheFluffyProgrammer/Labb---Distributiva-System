package webshop.ui;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import webshop.bo.ItemHandler;
import webshop.bo.OrderHandler;
import webshop.bo.ShopException;
import webshop.bo.ShoppingCart;
import webshop.bo.User;
import webshop.bo.UserHandler;

/** Det enda JSP-sidorna anropar. Håller reda på sessionen och anropar affärslagrets handlers. */
public class UiController {

    // Namn på attributen i HttpSession
    private static final String USER = "user";
    private static final String CART = "cart";

    private UiController() {
    }

    // Inloggning och behörighet ------

    /** Den inloggade användaren, eller null. */
    public static UserDTO getUser(HttpSession session) {
        return (UserDTO) session.getAttribute(USER);
    }

    public static boolean isLoggedIn(HttpSession session) {
        return getUser(session) != null;
    }

    public static boolean isAdmin(HttpSession session) {
        UserDTO user = getUser(session);
        return user != null && User.ADMIN.equals(user.getRole());
    }

    /** Lagerpersonal och admin får se lagret. */
    public static boolean isWarehouse(HttpSession session) {
        UserDTO user = getUser(session);
        return user != null && (User.WAREHOUSE.equals(user.getRole()) || User.ADMIN.equals(user.getRole()));
    }

    /** Returnerar null om inloggningen lyckades, annars ett felmeddelande. */
    public static String login(HttpServletRequest request) {
        try {
            UserDTO user = UserHandler.login(request.getParameter("username"), request.getParameter("password"));
            request.getSession().setAttribute(USER, user);
            return null;
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    public static void logout(HttpSession session) {
        session.invalidate(); // tar bort både användare och kundkorg
    }

    public static String register(HttpServletRequest request) {
        try {
            UserHandler.register(request.getParameter("username"), request.getParameter("password"));
            return "Kontot är skapat. Du kan nu logga in.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    // ---------- Varor och kundkorg ----------

    public static List<ItemDTO> getAllItems() throws ShopException {
        return ItemHandler.getAllItems();
    }

    public static String addToCart(HttpServletRequest request) {
        try {
            int itemId = toInt(request.getParameter("itemId"));
            int quantity = toInt(request.getParameter("quantity"));
            OrderHandler.addToCart(getCart(request.getSession()), itemId, quantity);
            return "Varan lades i kundkorgen.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    public static String removeFromCart(HttpServletRequest request) {
        try {
            OrderHandler.removeFromCart(getCart(request.getSession()), toInt(request.getParameter("itemId")));
            return "Varan togs bort.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    public static List<CartItemDTO> getCartItems(HttpSession session) {
        return OrderHandler.getCartItems(getCart(session));
    }

    public static int getCartTotal(HttpSession session) {
        return OrderHandler.getCartTotal(getCart(session));
    }

    /** Kundkorgen ligger i sessionen. Skapas första gången den behövs. */
    private static ShoppingCart getCart(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute(CART);
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute(CART, cart);
        }
        return cart;
    }

    // ---------- Ordrar ----------

    public static String placeOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!isLoggedIn(session)) {
            return "Du måste logga in för att skicka en order.";
        }
        try {
            int orderId = OrderHandler.placeOrder(getUser(session).getId(), getCart(session));
            return "Tack! Order nummer " + orderId + " är skickad till lagret och packas nu.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    public static List<OrderDTO> getMyOrders(HttpSession session) throws ShopException {
        return OrderHandler.getOrdersForUser(getUser(session).getId());
    }

    // ---------- Lager (lagerpersonal och admin) ----------

    public static String updateStock(HttpServletRequest request) {
        if (!isWarehouse(request.getSession())) {
            return "Du har inte behörighet.";
        }
        try {
            ItemHandler.updateStock(toInt(request.getParameter("itemId")), toInt(request.getParameter("stock")));
            return "Lagret är uppdaterat.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    /** Ordrarna som väntar på att skickas. */
    public static List<OrderDTO> getOrdersToShip() throws ShopException {
        return OrderHandler.getOrdersToShip();
    }

    public static String shipOrder(HttpServletRequest request) {
        if (!isWarehouse(request.getSession())) {
            return "Du har inte behörighet.";
        }
        try {
            int orderId = toInt(request.getParameter("orderId"));
            OrderHandler.shipOrder(orderId);
            return "Order nummer " + orderId + " är skickad.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    // ---------- Användare (bara admin) ----------

    public static String[] getRoles() {
        return new String[] {User.CUSTOMER, User.WAREHOUSE, User.ADMIN};
    }

    public static List<UserDTO> getAllUsers() throws ShopException {
        return UserHandler.getAllUsers();
    }

    public static String addUser(HttpServletRequest request) {
        if (!isAdmin(request.getSession())) {
            return "Du har inte behörighet.";
        }
        try {
            UserHandler.addUser(request.getParameter("username"), request.getParameter("password"),
                    request.getParameter("role"));
            return "Användaren är skapad.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    public static String setRole(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!isAdmin(session)) {
            return "Du har inte behörighet.";
        }
        try {
            UserHandler.setRole(getUser(session).getId(), toInt(request.getParameter("userId")),
                    request.getParameter("role"));
            return "Rollen är ändrad.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    public static String setActive(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!isAdmin(session)) {
            return "Du har inte behörighet.";
        }
        try {
            boolean active = "true".equals(request.getParameter("active"));
            UserHandler.setActive(getUser(session).getId(), toInt(request.getParameter("userId")), active);
            return active ? "Kontot är aktiverat." : "Kontot är avstängt.";
        } catch (ShopException e) {
            return e.getMessage();
        }
    }

    private static int toInt(String value) throws ShopException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ShopException("Ogiltigt tal.");
        }
    }
}
