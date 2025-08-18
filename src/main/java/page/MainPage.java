package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import page.modal.LoginModal;
import page.modal.SignUpModal;

import java.util.List;

import static utils.Constants.BASE_URL;

/**
 * Represents the main page of the application.
 * <p>
 * Provides methods to interact with the main page elements (username, login/logout buttons).
 */
public class MainPage {
    private final Page page;

    // Modal windows
    public final LoginModal loginModal;
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

        this.loginModal = new LoginModal(page);
        this.signUpModal = new SignUpModal(page);
    }

    /**
     * Navigate to the main page using the base URL.
     */
    public void navigate() {
        page.navigate(BASE_URL);
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

    public List<ProductCardItem> getAllDisplayedItems() {
        return page.locator("#tbodyid > div").all()
                .stream()
                .map(ProductCardItem::new)
                .toList();
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
