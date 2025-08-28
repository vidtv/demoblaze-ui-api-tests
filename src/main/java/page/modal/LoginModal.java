package page.modal;

import com.microsoft.playwright.Locator;

import static com.microsoft.playwright.options.AriaRole.BUTTON;

/**
 * Represents the 'Login' modal window of the application.
 * <p>
 * Provides methods to interact with the modal window elements
 * (username and password input fields, 'Log in' button)
 * and method to perform login action.
 */
public class LoginModal extends ModalWindow {

    public LoginModal(Locator locator) {
        super(locator);
    }

    /**
     * Returns the locator for the login modal title.
     *
     * @return locator for the login modal title
     */
    public Locator getLoginModalTitle() {
        return modalLocator.locator("#logInModalLabel");
    }

    /**
     * Fill the login form with the provided username and password
     * and click 'Log in' button.
     *
     * @param username the username to fill in
     * @param password the password to fill in
     */
    public void login(String username, String password) {
        modalLocator.locator("#loginusername").fill(username);
        modalLocator.locator("#loginpassword").fill(password);
        modalLocator.getByRole(BUTTON).getByText("Log in").click();
    }
}
