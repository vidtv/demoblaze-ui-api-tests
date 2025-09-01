package integration;

import base.BaseTest;
import com.microsoft.playwright.Dialog;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static page.modal.SignUpModal.SIGN_UP_MODAL_TITLE;

@Epic("Demoblaze")
@Feature("User Authentication and Registration")
public class AuthIntegrationTests extends BaseTest {
    private String signUpRequestBody;

    @BeforeEach
    void setUpTest() {
        page.onRequest(request -> {
            if (request.url().contains("signup")) {
                signUpRequestBody = request.postData();
            }
        });

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(Dialog::accept);
    }

    @Test
    void shouldCreateNewUserViaUiAndVerifyInApi() {
        var username = UUID.randomUUID() + "@playwright.com";
        var password = "testpass";

        step("1. Open the main application page, click 'Sign up' button and check that modal window is opened", () -> {
            mainPage.navigate();
            mainPage.getSignUpButton().click();

            assertThat(mainPage.signUpModal.getModalTitle()).hasText(SIGN_UP_MODAL_TITLE);
        });

        step("2. Populate username and password inputs with correct values and click 'Sign up' button", () -> {
            mainPage.signUpModal.getUsernameInput().fill(username);
            mainPage.signUpModal.getPasswordInput().fill(password);
            mainPage.signUpModal.getSignUpButton().click();
        });
    }
}
