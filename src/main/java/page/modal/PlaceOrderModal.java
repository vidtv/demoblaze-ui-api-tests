package page.modal;

import com.microsoft.playwright.Locator;

import static com.microsoft.playwright.options.AriaRole.BUTTON;

/**
 * Represents the 'Place Order' modal window of the application.
 * <p>
 * Provides methods to interact with the modal window elements
 * (name and credit card input fields, 'Purchase' button)
 * and method of population required checkout fields.
 */
public class PlaceOrderModal extends ModalWindow {

    public PlaceOrderModal(Locator locator) {
        super(locator);
    }

    /**
     * Get a locator for the 'Name' input field in the 'Place Order' modal window.
     *
     * @return locator for the 'Name' input field
     */
    public Locator getNameInput() {
        return modalLocator.locator("#name");
    }

    /**
     * Get a locator for the 'Credit Card' input field in the 'Place Order' modal window.
     *
     * @return locator for the 'Credit Card' input field
     */
    public Locator getCreditCardInput() {
        return modalLocator.locator("#card");
    }

    /**
     * Get a locator for the 'Purchase' button in the 'Place Order' modal window.
     *
     * @return locator for the 'Purchase' button
     */
    public Locator getPurchaseButton() {
        return modalLocator.getByRole(BUTTON).getByText("Purchase");
    }

    /**
     * Populate required fields in the 'Place Order' modal window
     * and click the 'Purchase' button.
     * <p>
     * Fills in the 'Name' field with "TestName" and the 'Credit Card' field with "1111222233334444",
     * then clicks the 'Purchase' button to submit the form.
     */
    public void populateOrderForm() {
        modalLocator.locator("#name").fill("TestName");
        modalLocator.locator("#card").fill("1111222233334444");

        modalLocator.getByRole(BUTTON).getByText("Purchase").click();
    }
}
