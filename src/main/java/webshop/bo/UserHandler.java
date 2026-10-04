package webshop.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webshop.ui.UserDTO;

/**
 * Affärslagrets metoder för inloggning och administration av användare.
 */
public class UserHandler {

    private UserHandler() {
    }

    /** Loggar in. Returnerar användaren eller kastar ShopException med ett felmeddelande. */
    public static UserDTO login(String username, String password) throws ShopException {
        if (username == null || password == null) {
            throw new ShopException("Fyll i användarnamn och lösenord.");
        }
        try {
            User user = User.login(username, password);
            if (user == null) {
                throw new ShopException("Fel användarnamn eller lösenord.");
            }
            if (!user.isActive()) {
                throw new ShopException("Kontot är avstängt.");
            }
            return toDTO(user);
        } catch (SQLException e) {
            throw new ShopException("Inloggningen misslyckades", e);
        }
    }

    /** Ny kund registrerar sig själv. */
    public static void register(String username, String password) throws ShopException {
        addUser(username, password, User.CUSTOMER);
    }

    /** Admin lägger till en användare med valfri roll. */
    public static void addUser(String username, String password, String role) throws ShopException {
        // Bara bokstäver, siffror och _ så att namnet alltid går att visa säkert i HTML.
        if (username == null || !username.matches("[A-Za-z0-9_]{3,30}")) {
            throw new ShopException("Användarnamnet ska vara 3-30 tecken: a-z, A-Z, 0-9 eller _.");
        }
        if (password == null || password.length() < 4) {
            throw new ShopException("Lösenordet ska vara minst 4 tecken.");
        }
        if (!User.isValidRole(role)) {
            throw new ShopException("Okänd roll.");
        }
        try {
            if (User.usernameExists(username)) {
                throw new ShopException("Användarnamnet är upptaget.");
            }
            User.addUser(username, password, role);
        } catch (SQLException e) {
            throw new ShopException("Kunde inte skapa användaren", e);
        }
    }

    public static List<UserDTO> getAllUsers() throws ShopException {
        try {
            List<UserDTO> result = new ArrayList<>();
            for (User user : User.getAllUsers()) {
                result.add(toDTO(user));
            }
            return result;
        } catch (SQLException e) {
            throw new ShopException("Kunde inte hämta användarna", e);
        }
    }

    /** adminId är den inloggade admin, som inte får ändra sitt eget konto. */
    public static void setRole(int adminId, int userId, String role) throws ShopException {
        if (adminId == userId) {
            throw new ShopException("Du kan inte ändra din egen roll.");
        }
        if (!User.isValidRole(role)) {
            throw new ShopException("Okänd roll.");
        }
        try {
            User.updateRole(userId, role);
        } catch (SQLException e) {
            throw new ShopException("Kunde inte ändra rollen", e);
        }
    }

    public static void setActive(int adminId, int userId, boolean active) throws ShopException {
        if (adminId == userId) {
            throw new ShopException("Du kan inte stänga av ditt eget konto.");
        }
        try {
            User.updateActive(userId, active);
        } catch (SQLException e) {
            throw new ShopException("Kunde inte ändra kontot", e);
        }
    }

    private static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole(), user.isActive());
    }
}
