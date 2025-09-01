package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import page.modal.*;

import java.util.List;

import static utils.Constants.BASE_URL;

/**
 * Represents the main page of the application.
 * <p>
 * Provides methods to interact with the main page elements
 * (such as login, logout, product selection, and navigation buttons)
 */
public class MainPage {
    private final Page page;

    // Modal windows
    public final LoginModal loginModal;
    public final NewMessageModal newMessageModal;
    public final AboutUsModal aboutUsModal;
    public final SignUpModal signUpModal;

    // Error alert messages
    public static final String WRONG_PASSWORD_ERROR = "Wrong password.";
    public static final String USER_DOES_NOT_EXISTS_ERROR = "User does not exist.";

    // Product categories
    public static final String PHONES_CATEGORY = "Phones";
    public static final String LAPTOPS_CATEGORY = "Laptops";
    public static final String MONITORS_CATEGORY = "Monitors";

    public MainPage(Page page) {
        this.page = page;

        this.loginModal = new LoginModal(page.locator("#logInModal"));
        this.newMessageModal = new NewMessageModal(page.locator("#exampleModal"));
        this.aboutUsModal = new AboutUsModal(page.locator("#videoModal"));
        this.signUpModal = new SignUpModal(page.locator("#signInModal"));
    }

    /**
     * Navigate to the main page using the base URL.
     */
    public void navigate() {
        page.navigate(BASE_URL);

        // Need to wait until a list of products is loaded
        page.waitForCondition(() -> !getDisplayedItemNames().isEmpty());
    }

    public Locator getSignUpButton() {
        return page.locator("#signin2");
    }

    /**
     * Get a locator for the 'Log in' button.
     * <p>
     * This button is visible only when a user is not logged in.
     *
     * @return locator for the 'Log in' button.
     */
    public Locator getLoginButton() {
        return page.locator("#login2");
    }

    /**
     * Get a locator for the 'Contact' button.
     *
     * @return locator for the 'Contact' button.
     */
    public Locator getContactButton() {
        return page.locator("[data-target='#exampleModal']");
    }

    /**
     * Get a locator for the 'About us' button.
     *
     * @return locator for the 'About us' button.
     */
    public Locator getAboutUsButton() {
        return page.locator("[data-target='#videoModal']");
    }

    /**
     * Get a locator for a username of a logged-in user.
     * <p>
     * This element is visible only when a user is logged in.
     *
     * @return locator for the username element.
     */
    public Locator getUsername() {
        return page.locator("#nameofuser");
    }

    /**
     * Get a locator for the 'Log out' button.
     * <p>
     * This button is visible only when a user is logged in.
     *
     * @return locator for the 'Log out' button.
     */
    public Locator getLogoutButton() {
        return page.locator("#logout2");
    }

    /**
     * Get a locator for 'Previous' button.
     * Used to open a previous page of a products list.
     *
     * @return locator for 'Previous' button
     */
    public Locator getPreviousPageButton() {
        return page.locator("#prev2");
    }

    /**
     * Get a locator for 'Next' button.
     * Used to open a next page of a products list.
     *
     * @return locator for 'Next' button
     */
    public Locator getNextPageButton() {
        return page.locator("#next2");
    }

    /**
     * Get a product category according to its name.
     *
     * @param  categoryName a name of product category
     * @return locator for a product category
     */
    public Locator getCategory(String categoryName) {
        return page.locator("#itemc").getByText(categoryName);
    }

    /**
     * Get a list of all displayed products on the main page.
     * Each product is represented as a ProductCardItem object.
     *
     * @return a list of all displayed products on the main page
     */
    public List<ProductCardItem> getAllDisplayedItems() {
        return page.locator("#tbodyid > div").all()
                .stream()
                .map(ProductCardItem::new)
                .toList();
    }

    /**
     * Get a product by its name.
     * If the product with the specified name is not found, a RuntimeException is thrown.
     *
     * @param productName the name of the product to find
     * @return the ProductCardItem with the specified name
     */
    public ProductCardItem getProductByName(String productName) {
        return getAllDisplayedItems()
                .stream()
                .filter(product -> product.getProductItemName().textContent().equals(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product with name '" + productName + "' not found"));
    }

    /**
     * Select a product by its name.
     * If the product with the specified name is found, it clicks on the product to select it.
     * If the product is not found, no action is taken.
     *
     * @param productName the name of the product to select
     */
    public void selectProductByName(String productName) {
        getAllDisplayedItems()
                .stream()
                .filter(product -> product.getProductItemName().textContent().equals(productName))
                .findFirst()
                .ifPresent(product -> product.getProductItemName().click());
    }

    /**
     * Get a list of all displayed products names.
     *
     * @return a list of all displayed products names
     */
    public List<String> getDisplayedItemNames() {
        return page.locator(".hrefch").allTextContents();
    }
}
