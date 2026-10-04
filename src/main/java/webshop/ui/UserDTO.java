package webshop.ui;

/** Den information om en användare som vyn får se (inget lösenord). */
public class UserDTO {

    private final int id;
    private final String username;
    private final String role;
    private final boolean active;

    public UserDTO(int id, String username, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.active = active;
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
