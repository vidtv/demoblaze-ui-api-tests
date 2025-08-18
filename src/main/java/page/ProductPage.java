package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductPage {
    private final Page page;

    public ProductPage(Page page) {
        this.page = page;
    }

    public Locator getProductName() {
        return page.locator("#tbodyid h2");
    }

    public Locator getProductPrice() {
        return page.locator(".price-container");
    }

    public Locator getProductDescription() {
        return page.locator("#more-information > p");
    }
}
