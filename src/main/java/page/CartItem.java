package page;

import com.microsoft.playwright.Locator;

/**
 * A class that represents a single product item in the cart.
 * <p>
 * It has methods to get a price and 'Delete' button for a single cart item.
 */
public class CartItem {
    private final Locator cartItemSelector;

    /**
     * Constructor for CartItem.
     *
     * @param cartItemSelector locator for the cart item
     */
    public CartItem(Locator cartItemSelector) {
        this.cartItemSelector = cartItemSelector;
    }

    /**
     * Return the locator for the cart item.
     *
     * @return locator for the cart item
     */
    public Locator getSelf() {
        return cartItemSelector;
    }

    /**
     * Return the locator for the 'Delete' button of the cart item.
     *
     * @return locator for the 'Delete' button
     */
    public Locator getDeleteButton() {
        return cartItemSelector.locator("//a[text()='Delete']");
    }

    /**
     * Return the name of the cart item.
     *
     * @return name of the cart item
     */
    public Locator getName() {
        return cartItemSelector.locator("td:nth-child(2)");
    }

    /**
     * Return the price of the cart item.
     *
     * @return price of the cart item
     */
    public String getPrice() {
        return cartItemSelector.locator("td:nth-child(3)").textContent();
    }
}
