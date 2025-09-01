package page.modal;

import com.microsoft.playwright.Locator;

/**
 * 'Sign up' modal window.
 * Opens after clicking 'Sign up' button in header.
 * </br>
 * Contains input fields for username and password,
 * and a button to submit the sign-up form.
 */
public class SignUpModal extends ModalWindow {

    // Modal window title
    public static final String SIGN_UP_MODAL_TITLE = "Sign up";

    /**
     * Constructor for a Sign up modal window.
     *
     * @param modalLocator the locator representing the modal window
     */
    public SignUpModal(Locator modalLocator) {
        super(modalLocator);
    }

    /**
     * Get a locator for the 'Username' input field in the modal window.
     *
     * @return locator for the 'Username' input field
     */
    public Locator getUsernameInput() {
        return modalLocator.locator("#sign-username");
    }

    /**
     * Get a locator for the 'Password' input field in the modal window.
     *
     * @return locator for the 'Password' input field
     */
    public Locator getPasswordInput() {
        return modalLocator.locator("#sign-password");
    }

    /**
     * Get a locator for the 'Sign up' button in the modal window.
     *
     * @return locator for the 'Sign up' button
     */
    public Locator getSignUpButton() {
        return modalLocator.locator("[onclick='register()']");
    }
}
