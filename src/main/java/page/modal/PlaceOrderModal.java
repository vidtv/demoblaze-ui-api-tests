package page.modal;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.options.AriaRole.BUTTON;

public class PlaceOrderModal extends ModalWindow {
    public PlaceOrderModal(Page page) {
        super(page);
    }

    public void populateOrderForm() {
        page.locator("#name").fill("TestName");
        page.locator("#country").fill("Hungary");
        page.locator("#city").fill("Budapest");
        page.locator("#card").fill("1111222233334444");
        page.locator("#month").fill("August");
        page.locator("#year").fill("2025");

        page.getByRole(BUTTON).getByText("Purchase").click();
    }
}
