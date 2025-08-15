package page.modal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.options.AriaRole.BUTTON;

public class LoginModal extends ModalWindow {

    public LoginModal(Page page) {
        super(page);
    }

    /**
     * Returns the locator for the login modal title.
     *
     * @return locator for the login modal title
     */
    public Locator getLoginModalTitle() {
        return page.locator("#logInModalLabel");
    }

    /**
     * Fill the login form with the provided username and password
     * and click 'Log in' button.
     *
     * @param username the username to fill in
     * @param password the password to fill in
     */
    public void login(String username, String password) {
        page.fill("#loginusername", username);
        page.fill("#loginpassword", password);
        page.getByRole(BUTTON).getByText("Log in").click();
    }
}
