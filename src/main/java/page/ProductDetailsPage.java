package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static utils.Constants.BASE_URL;

/**
 * Represents the product details page of the application.
 * <p>
 * Provides methods to interact with the product details page elements
 * (product name, price, description, 'Add to cart' button).
 */
public class ProductDetailsPage {
    private final Page page;

    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    public void openProductDetailsPage(int productId) {
        page.navigate(BASE_URL + "/prod.html?idp_=" + productId);
    }

    // Alert messages
    public static final String PRODUCT_ADDED_ALERT_MESSAGE = "Product added";

    /**
     * Get a locator for the product name element.
     *
     * @return locator for the product name element
     */
    public Locator getProductName() {
        return page.locator("#tbodyid h2");
    }

    /**
     * Get a locator for the product price element.
     *
     * @return locator for the product price element
     */
    public String getProductPrice() {
        return page.locator(".price-container").textContent()
                .replace("$", "")
                .replace(" *includes tax", "");
    }

    /**
     * Get a locator for the product description paragraph element.
     *
     * @return locator for the product description paragraph element
     */
    public Locator getProductDescription() {
        return page.locator("#more-information > p");
    }

    /**
     * Get a locator for the 'Add to cart' button.
     *
     * @return locator for the 'Add to cart' button
     */
    public Locator getAddToCartButton() {
        return page.getByText("Add to cart");
    }
}
