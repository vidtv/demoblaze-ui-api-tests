package ui;

import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.MainPage.USER_DOES_NOT_EXISTS_ERROR;
import static page.MainPage.WRONG_PASSWORD_ERROR;
import static utils.Constants.PASSWORD;
import static utils.Constants.USERNAME;

@Epic("Demoblaze")
@Feature("Login and Logout")
public class LoginAndLogoutTests extends BaseTest {

    @Test
    @DisplayName("Login with valid credentials and user logout")
    @Description("Verify successful login with valid credentials")
    public void successfulLoginAndLogoutTest() {
        step("1. Open the main page, click 'Log in' button and verify that login modal window is opened",
                this::openLoginModalWindow
        );

        step("2. Enter correct user credentials, click 'Log in' button " +
                "and verify that username is displayed in the header", () -> {
            mainPage.loginModal.login(USERNAME, PASSWORD);

            assertThat(mainPage.getUsername()).hasText("Welcome " + USERNAME);
        });

        step("3. Click 'Log out' button and verify that 'Log in' button is displayed " +
                "and username is not displayed in the header", () -> {
            mainPage.getLogoutButton().click();

            assertThat(mainPage.getLoginButton()).isVisible();
            assertThat(mainPage.getUsername()).isHidden();
        });
    }

    @Test
    @DisplayName("Login with invalid credentials")
    @Description("Verify that there are error alerts in case of invalid login or password")
    public void loginWithIncorrectCredentialsTest() {
        step("1. Open the main page, click 'Log in' button and verify that login modal window is opened",
                this::openLoginModalWindow
        );

        step("2. Enter correct username and incorrect password, click 'Log in' button and verify that error alert appeared, " +
                "accept the alert and check that username is not displayed on the main page", () -> {
            var incorrectPassword = "incorrectPassword";

            mainPage.loginModal.login(USERNAME, incorrectPassword);

            page.onDialog(dialog -> {
                assertEquals(WRONG_PASSWORD_ERROR, dialog.message());
                dialog.accept();
            });

            assertThat(mainPage.getUsername()).isHidden();
        });

        step("3. Enter random username, click 'Log in' button and verify that error alert appeared, " +
                "accept the alert and check that username is not displayed on the main page", () -> {
            var randomUsername = UUID.randomUUID() + "@test.com";

            mainPage.loginModal.login(randomUsername, PASSWORD);
            page.onDialog(dialog -> {
                assertEquals(USER_DOES_NOT_EXISTS_ERROR, dialog.message());
                dialog.accept();
            });

            assertThat(mainPage.getUsername()).isHidden();
        });
    }

    @Step
    private void openLoginModalWindow() {
        mainPage.navigate();
        mainPage.getLoginButton().click();

        assertThat(mainPage.loginModal.getLoginModalTitle()).isVisible();
    }
}
