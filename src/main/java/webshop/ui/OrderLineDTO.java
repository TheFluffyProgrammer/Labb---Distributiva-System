package webshop.ui;

/**
 * En rad i en order som den visas för användaren.
 */
public class OrderLineDTO {

    private final String itemName;
    private final int quantity;
    private final int price;

    public OrderLineDTO(String itemName, int quantity, int price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
