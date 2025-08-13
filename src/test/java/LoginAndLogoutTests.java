import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static utils.Constants.PASSWORD;
import static utils.Constants.USERNAME;

@Epic("Demoblaze")
@Feature("Login and Logout")
public class LoginAndLogoutTests extends BaseTest {

    @Test
    @DisplayName("Login with valid credentials")
    @Description("Verify successful login with valid credentials")
    public void successfulLoginTest() {
        step("1. Open the main page, click 'Log in' button and verify that login modal window is opened", () -> {
            mainPage.navigate();
            mainPage.getLoginButton().click();

            assertThat(mainPage.loginModal.getLoginModalTitle()).isVisible();
        });

        step("2. Enter correct user credentials, click 'Log in' button " +
                "and verify that username is displayed in the header", () -> {
            mainPage.loginModal.login(USERNAME, PASSWORD);

            assertThat(mainPage.getUsername()).hasText("Welcome " + USERNAME);
        });
    }

    // TBD
    public void loginWithIncorrectCredentialsTest() {

    }

    // TBD
    public void logoutTest() {

    }
}
