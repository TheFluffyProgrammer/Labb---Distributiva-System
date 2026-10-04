package webshop.bo;

/**
 * En rad i kundkorgen: en vara och hur många av den.
 */
public class CartItem {

    private final Item item;
    private int quantity;

    public CartItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    void addQuantity(int more) {
        quantity += more;
    }

    public int getTotal() {
        return item.getPrice() * quantity;
    }
}
