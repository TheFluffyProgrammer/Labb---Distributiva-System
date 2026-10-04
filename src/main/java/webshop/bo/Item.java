package webshop.bo;

import java.sql.SQLException;
import java.util.List;

import webshop.db.DBItem;

/**
 * En vara i butiken. Hämtas och sparas via DBItem (som ärver denna klass).
 */
public class Item {

    private final int id;
    private final String name;
    private final String description;
    private final String category;
    private final int price;
    private final int stock;

    protected Item(int id, String name, String description, String category, int price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    static List<Item> getAllItems() throws SQLException {
        return DBItem.getAllItems();
    }

    static Item getItem(int id) throws SQLException {
        return DBItem.getItem(id);
    }

    static void updateStock(int id, int stock) throws SQLException {
        DBItem.updateStock(id, stock);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
