import base.BaseTest;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static page.modal.ContactModal.CONTACT_MODAL_TITLE;

public class ModalTests extends BaseTest {

    @Test
    void contactModalTest() {
        step("1. Open the main application page, click 'Contact' button and check that Contact modal window is opened " +
                "with expected input fields", () -> {
            mainPage.navigate();
            mainPage.getContactButton().click();

            assertThat(mainPage.contactModal.getModalTitle()).hasText(CONTACT_MODAL_TITLE);
            assertThat(mainPage.contactModal.getContactEmailInput()).isVisible();
            assertThat(mainPage.contactModal.getContactNameInput()).isVisible();
            assertThat(mainPage.contactModal.getContactMessage()).isVisible();
        });

        step("2. Click 'X' button in the modal window and check that the modal window is hidden", () -> {
            mainPage.contactModal.getHeaderCloseButton().click();

            assertThat(mainPage.contactModal.getModalTitle()).isHidden();
            assertThat(mainPage.contactModal.getContactEmailInput()).isHidden();
            assertThat(mainPage.contactModal.getContactNameInput()).isHidden();
            assertThat(mainPage.contactModal.getContactMessage()).isHidden();
        });

        step("3. Click 'Contact' button again and check that Contact modal window is opened " +
                "with expected input fields", () -> {
            mainPage.getContactButton().click();

            assertThat(mainPage.contactModal.getModalTitle()).hasText(CONTACT_MODAL_TITLE);
            assertThat(mainPage.contactModal.getContactEmailInput()).isVisible();
            assertThat(mainPage.contactModal.getContactNameInput()).isVisible();
            assertThat(mainPage.contactModal.getContactMessage()).isVisible();
        });

        step("4. Click 'Close' button in the modal window and check that the modal window is hidden", () -> {
            mainPage.contactModal.getCloseButton().click();

            assertThat(mainPage.contactModal.getModalTitle()).isHidden();
            assertThat(mainPage.contactModal.getContactEmailInput()).isHidden();
            assertThat(mainPage.contactModal.getContactNameInput()).isHidden();
            assertThat(mainPage.contactModal.getContactMessage()).isHidden();
        });
    }
}
