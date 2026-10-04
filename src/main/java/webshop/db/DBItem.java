package webshop.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webshop.bo.Item;

/** Databasdelen av en vara. Ärver Item. */
public class DBItem extends Item {

    private DBItem(int id, String name, String description, String category, int price, int stock) {
        super(id, name, description, category, price, stock);
    }

    public static List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM items ORDER BY category, name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(createItem(rs));
            }
        }
        return items;
    }

    /** Returnerar null om varan inte finns. */
    public static Item getItem(int id) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM items WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createItem(rs);
                }
                return null;
            }
        }
    }

    public static void updateStock(int id, int stock) throws SQLException {
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE items SET stock = ? WHERE id = ?")) {
            ps.setInt(1, stock);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private static Item createItem(ResultSet rs) throws SQLException {
        return new DBItem(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                rs.getString("category"), rs.getInt("price"), rs.getInt("stock"));
    }
}
