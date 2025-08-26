package page.modal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.options.AriaRole.DIALOG;

public class ContactModal extends ModalWindow {

    public ContactModal(Page page) {
        super(page);
    }

    // Modal title
    public static final String CONTACT_MODAL_TITLE = "New message";

    public Locator getModalTitle() {
        return page.locator("#exampleModalLabel");
    }

    public Locator getHeaderCloseButton() {
        return page.getByRole(DIALOG, new Page.GetByRoleOptions().setName(CONTACT_MODAL_TITLE)).getByLabel("Close");
    }

    public Locator getContactEmailInput() {
        return page.getByLabel("Contact Email:");
    }

    public Locator getContactNameInput() {
        return page.getByLabel("Contact Name:");
    }

    public Locator getContactMessage() {
        return page.getByLabel("Message:");
    }

    public Locator getCloseButton() {
        return page.getByLabel(CONTACT_MODAL_TITLE).getByText("Close");
    }
}
