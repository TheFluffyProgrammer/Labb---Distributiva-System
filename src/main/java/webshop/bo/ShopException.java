package webshop.bo;

/** Fel som ska visas för användaren, t.ex. "slut i lager". */
public class ShopException extends Exception {

    public ShopException(String message) {
        super(message);
    }

    public ShopException(String message, Throwable cause) {
        super(message, cause);
    }
}
