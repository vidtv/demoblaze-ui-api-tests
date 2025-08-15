package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import page.modal.LoginModal;
import page.modal.SignUpModal;

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
}
