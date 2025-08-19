package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductPage {
    private final Page page;

    public ProductPage(Page page) {
        this.page = page;
    }

    // Alert messages
    public static final String PRODUCT_ADDED_ALERT_MESSAGE = "Product added";

    public Locator getProductName() {
        return page.locator("#tbodyid h2");
    }

    public Locator getProductPrice() {
        return page.locator(".price-container");
    }

    public Locator getProductDescription() {
        return page.locator("#more-information > p");
    }

    public Locator getAddToCartButton() {
        return page.getByText("Add to cart");
    }
}
