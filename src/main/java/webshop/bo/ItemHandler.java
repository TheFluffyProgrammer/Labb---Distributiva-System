package webshop.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webshop.ui.ItemDTO;

/** Affärslagrets metoder för varor och varulager. */
public class ItemHandler {

    private ItemHandler() {
    }

    public static List<ItemDTO> getAllItems() throws ShopException {
        try {
            List<ItemDTO> result = new ArrayList<>();
            for (Item item : Item.getAllItems()) {
                result.add(toDTO(item));
            }
            return result;
        } catch (SQLException e) {
            throw new ShopException("Kunde inte hämta varorna", e);
        }
    }

    /** Sätter nytt lagersaldo (används av lagerpersonal). */
    public static void updateStock(int itemId, int stock) throws ShopException {
        if (stock < 0) {
            throw new ShopException("Lagersaldot kan inte vara negativt.");
        }
        try {
            if (Item.getItem(itemId) == null) {
                throw new ShopException("Varan finns inte.");
            }
            Item.updateStock(itemId, stock);
        } catch (SQLException e) {
            throw new ShopException("Kunde inte uppdatera lagret", e);
        }
    }

    private static ItemDTO toDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(),
                item.getCategory(), item.getPrice(), item.getStock());
    }
}
