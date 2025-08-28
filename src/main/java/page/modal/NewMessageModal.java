package page.modal;

import com.microsoft.playwright.Locator;

/**
 * 'New message' modal window.
 * Opens after clicking 'Contact' button in header.
 * </br>
 * Contains input fields for contact email, name, and message.
 */
public class NewMessageModal extends ModalWindow {

    // Modal title
    public static final String CONTACT_MODAL_TITLE = "New message";

    public NewMessageModal(Locator locator) {
        super(locator);
    }

    /**
     * Get a locator for the contact email input field.
     *
     * @return locator for the contact email input field
     */
    public Locator getContactEmailInput() {
        return modalLocator.getByLabel("Contact Email:");
    }

    /**
     * Get a locator for the contact name input field.
     *
     * @return locator for the contact name input field
     */
    public Locator getContactNameInput() {
        return modalLocator.getByLabel("Contact Name:");
    }

    /**
     * Get a locator for the message input field.
     *
     * @return locator for the message input field
     */
    public Locator getContactMessage() {
        return modalLocator.getByLabel("Message:");
    }
}
