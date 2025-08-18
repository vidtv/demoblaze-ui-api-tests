package page;

import com.microsoft.playwright.Locator;

public class ProductCardItem {
    private final Locator item;

    public ProductCardItem(Locator item) {
        this.item = item;
    }

    public Locator getProductItemName() {
        return item.locator(".card-title > a");
    }

    public String getProductItemPrice() {
        return item.locator("h5").textContent();
    }

    public String getProductItemDescription() {
        return item.locator("#article").textContent();
    }
}
