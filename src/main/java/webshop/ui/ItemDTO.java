package webshop.ui;

/** Den information om en vara som vyn får se. */
public class ItemDTO {

    private final int id;
    private final String name;
    private final String description;
    private final String category;
    private final int price;
    private final int stock;

    public ItemDTO(int id, String name, String description, String category, int price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
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

    public boolean isInStock() {
        return stock > 0;
    }
}
