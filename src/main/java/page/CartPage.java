package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import page.modal.PlaceOrderModal;

import java.util.List;

import static utils.Constants.BASE_URL;

/**
 * Represents cart page of the application.
 * <p>
 * Provides methods to interact with cart page elements
 * (added product data, 'Delete' and 'Place Order' buttons).
 */
public class CartPage {
    private final Page page;

    // Messages
    public static final String THANK_YOU_FOR_YOUR_PURCHASE = "Thank you for your purchase!";

    // Alert messages
    public static final String PLEASE_FILL_OUT_NAME_AND_CREDIT_CARD = "Please fill out Name and Creditcard.";

    // Modal windows
    public final PlaceOrderModal placeOrderModal;

    public CartPage(Page page) {
        this.page = page;

        this.placeOrderModal = new PlaceOrderModal(page);
    }

    /**
     * Navigate to the cart page using the base URL.
     */
    public void navigate() {
        page.navigate(BASE_URL + "/cart.html");
    }

    /**
     * Get a cart item by its name.
     *
     * @return cart item with the specified name
     */
    public CartItem getCartItemByName(String cartItemName) {
        return new CartItem(page.locator(String.format("//tr[td[text()='%s']]", cartItemName)));
    }

    /**
     * Get a locator for a total sum of order.
     *
     * @return locator for a total sum of order
     */
    public Locator getTotalOrderSum() {
        return page.locator("#totalp");
    }

    public List<CartItem> getAllCartItems() {
        return page.locator(".success").all()
                .stream()
                .map(CartItem::new)
                .toList();
    }

    public Locator getPlaceOrderButton() {
        return page.locator("[data-target='#orderModal']");
    }

    public Locator getPurchaseCompleteAlert() {
        return page.locator(".sweet-alert");
    }

    public Locator getSuccessfulPurchaseCompleteIcon() {
        return page.locator(".sa-icon.sa-success");
    }

    public Locator getPurchaseCompleteMessage() {
        return page.locator(".sweet-alert h2");
    }
}
