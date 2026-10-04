package webshop.bo;

/**
 * En rad i en order: vilken vara, hur många och priset när ordern skickades.
 */
public class OrderLine {

    private final String itemName;
    private final int quantity;
    private final int price;

    public OrderLine(String itemName, int quantity, int price) {
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
