package webshop.ui;

/**
 * En rad i kundkorgen som den visas för användaren.
 */
public class CartItemDTO {

    private final int itemId;
    private final String name;
    private final int price;
    private final int quantity;

    public CartItemDTO(int itemId, String name, int price, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotal() {
        return price * quantity;
    }
}
