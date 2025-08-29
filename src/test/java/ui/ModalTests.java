package ui;

import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static page.modal.AboutUsModal.ABOUT_US_MODAL_TITLE;
import static page.modal.NewMessageModal.CONTACT_MODAL_TITLE;

@Epic("Demoblaze")
@Feature("Modal windows")
public class ModalTests extends BaseTest {

    @Test
    @DisplayName("'New message' modal window test")
    @Description("Verify that 'New message' modal window is opened by clicking 'Contact' button")
    void newMessageModalTest() {
        step("1. Open the main application page, click 'Contact' button and check that 'New message' modal window is opened " +
                "with expected input fields", () -> {
            mainPage.navigate();
            mainPage.getContactButton().click();

            assertThat(mainPage.newMessageModal.getModalTitle()).hasText(CONTACT_MODAL_TITLE);
            assertThat(mainPage.newMessageModal.getContactEmailInput()).isVisible();
            assertThat(mainPage.newMessageModal.getContactNameInput()).isVisible();
            assertThat(mainPage.newMessageModal.getContactMessage()).isVisible();
        });

        step("2. Click 'X' button in the modal window and check that the modal window is hidden", () -> {
            mainPage.newMessageModal.getHeaderCloseButton().click();

            assertThat(mainPage.newMessageModal.getModalTitle()).isHidden();
            assertThat(mainPage.newMessageModal.getContactEmailInput()).isHidden();
            assertThat(mainPage.newMessageModal.getContactNameInput()).isHidden();
            assertThat(mainPage.newMessageModal.getContactMessage()).isHidden();
        });

        step("3. Click 'Contact' button again and check that 'New message' modal window is opened " +
                "with expected input fields", () -> {
            mainPage.getContactButton().click();

            assertThat(mainPage.newMessageModal.getModalTitle()).hasText(CONTACT_MODAL_TITLE);
            assertThat(mainPage.newMessageModal.getContactEmailInput()).isVisible();
            assertThat(mainPage.newMessageModal.getContactNameInput()).isVisible();
            assertThat(mainPage.newMessageModal.getContactMessage()).isVisible();
        });

        step("4. Click 'Close' button in the modal window and check that the modal window is hidden", () -> {
            mainPage.newMessageModal.getCloseButton().click();

            assertThat(mainPage.newMessageModal.getModalTitle()).isHidden();
            assertThat(mainPage.newMessageModal.getContactEmailInput()).isHidden();
            assertThat(mainPage.newMessageModal.getContactNameInput()).isHidden();
            assertThat(mainPage.newMessageModal.getContactMessage()).isHidden();
        });
    }

    @Test
    @DisplayName("'About us' modal window test")
    @Description("Verify that 'About us' modal window is opened by clicking 'About us' button")
    void aboutUsModalTest() {
        step("1. Open the main application page, click 'About us' button and check that 'About us' modal window is opened " +
                "with expected title and displayed video element", () -> {
            mainPage.navigate();
            mainPage.getAboutUsButton().click();

            assertThat(mainPage.aboutUsModal.getModalTitle()).hasText(ABOUT_US_MODAL_TITLE);
            assertThat(mainPage.aboutUsModal.getAboutUsVideo()).isVisible();
        });

        step("2. Click 'X' button in the modal window and check that the modal window is hidden", () -> {
            mainPage.aboutUsModal.getHeaderCloseButton().click();

            assertThat(mainPage.aboutUsModal.getModalTitle()).isHidden();
            assertThat(mainPage.aboutUsModal.getAboutUsVideo()).isHidden();
        });

        step("3. Click 'About us' button again and check that 'About us' modal window is opened " +
                "with expected title and displayed video element", () -> {
            mainPage.getAboutUsButton().click();

            assertThat(mainPage.aboutUsModal.getModalTitle()).hasText(ABOUT_US_MODAL_TITLE);
            assertThat(mainPage.aboutUsModal.getAboutUsVideo()).isVisible();
        });

        step("4. Click 'Close' button in the modal window and check that the modal window is hidden", () -> {
            mainPage.aboutUsModal.getCloseButton().click();

            assertThat(mainPage.aboutUsModal.getModalTitle()).isHidden();
            assertThat(mainPage.aboutUsModal.getAboutUsVideo()).isHidden();
        });
    }
}
