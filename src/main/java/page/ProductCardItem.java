package page;

import com.microsoft.playwright.Locator;

/**
 * Represents a product card item on the main page.
 * <p>
 * Provides methods to get the product card item elements (name, price, description).
 */
public class ProductCardItem {
    private final Locator item;

    public ProductCardItem(Locator item) {
        this.item = item;
    }

    /**
     * Get a locator for the product item name link.
     *
     * @return locator for the product item name link
     */
    public Locator getProductItemName() {
        return item.locator(".card-title > a");
    }

    /**
     * Get the product item price as a string without the currency sign.
     *
     * @return product item price without the currency sign
     */
    public String getProductItemPrice() {
        return item.locator("h5").textContent().replace("$", "");
    }

    /**
     * Get the product item description text.
     *
     * @return product item description text
     */
    public String getProductItemDescription() {
        return item.locator("#article").textContent();
    }
}
