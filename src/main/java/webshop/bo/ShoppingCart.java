package webshop.bo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Kundkorgen. Sparas i användarens HttpSession, så varje användare har sin egen.
 */
public class ShoppingCart {

    // varans id -> rad i korgen
    private final Map<Integer, CartItem> items = new LinkedHashMap<>();

    void add(Item item, int quantity) {
        CartItem existing = items.get(item.getId());
        if (existing == null) {
            items.put(item.getId(), new CartItem(item, quantity));
        } else {
            existing.addQuantity(quantity);
        }
    }

    void remove(int itemId) {
        items.remove(itemId);
    }

    void clear() {
        items.clear();
    }

    /** Hur många av en viss vara som redan ligger i korgen. */
    int getQuantity(int itemId) {
        CartItem ci = items.get(itemId);
        return ci == null ? 0 : ci.getQuantity();
    }

    List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    int getTotal() {
        int total = 0;
        for (CartItem ci : items.values()) {
            total += ci.getTotal();
        }
        return total;
    }

    boolean isEmpty() {
        return items.isEmpty();
    }
}
