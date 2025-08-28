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

        this.placeOrderModal = new PlaceOrderModal(page.locator("#orderModal"));
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

    /**
     * Get a list of all cart items currently in the cart.
     *
     * @return list of all cart items
     */
    public List<CartItem> getAllCartItems() {
        return page.locator(".success").all()
                .stream()
                .map(CartItem::new)
                .toList();
    }

    /**
     * Get a locator for the 'Place Order' button.
     *
     * @return locator for the 'Place Order' button
     */
    public Locator getPlaceOrderButton() {
        return page.locator("[data-target='#orderModal']");
    }

    /**
     * Get a locator for the alert that appears after completing a purchase.
     *
     * @return locator for the purchase complete alert
     */
    public Locator getPurchaseCompleteAlert() {
        return page.locator(".sweet-alert");
    }

    /**
     * Get a locator for the success icon in the purchase complete alert.
     *
     * @return locator for the success icon
     */
    public Locator getSuccessfulPurchaseCompleteIcon() {
        return page.locator(".sa-icon.sa-success");
    }

    /**
     * Get a locator for the purchase complete message in the alert.
     *
     * @return locator for the purchase complete message
     */
    public Locator getPurchaseCompleteMessage() {
        return page.locator(".sweet-alert h2");
    }
}
