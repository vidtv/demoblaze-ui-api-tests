package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Represents cart page of the application.
 * <p>
 * Provides methods to interact with cart page elements
 * (added product data, 'Delete' and 'Place Order' buttons).
 */
public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public Locator getProductName() {
        return page.locator(".success td:nth-child(2)");
    }

    public Locator getProductPrice() {
        return page.locator(".success td:nth-child(3)");
    }
}
