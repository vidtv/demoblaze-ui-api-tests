package page.modal;

import com.microsoft.playwright.Locator;

/**
 * Base class representing a generic modal window in the application.
 * <p>
 * Provides common methods to interact with modal window elements
 * such as modal title and close buttons.
 */
public abstract class ModalWindow {
    protected Locator modalLocator;

    /**
     * Constructor for ModalWindow.
     *
     * @param modalLocator the locator representing the modal window
     */
    public ModalWindow(Locator modalLocator) {
        this.modalLocator = modalLocator;
    }

    /**
     * Get a locator for the modal title element.
     *
     * @return locator for the modal title
     */
    public Locator getModalTitle() {
        return modalLocator.locator(".modal-title");
    }

    /**
     * Get a locator for the header close button (the 'X' button).
     *
     * @return locator for the header close button
     */
    public Locator getHeaderCloseButton() {
        return modalLocator.locator(".close");
    }

    /**
     * Get a locator for the footer close button (the 'Close' button).
     *
     * @return locator for the footer close button
     */
    public Locator getCloseButton() {
        return modalLocator.getByText("Close", new Locator.GetByTextOptions().setExact(true));
    }
}
