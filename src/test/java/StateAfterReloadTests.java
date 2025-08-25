import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static utils.Constants.PASSWORD;
import static utils.Constants.USERNAME;

@Epic("Demoblaze")
@Feature("Session Persistence")
public class StateAfterReloadTests extends BaseTest {

    @Test
    @DisplayName("Login state persists after page reload")
    @Description("Verify that user remains logged in after refreshing the page")
    void sessionPersistsAfterReloadTest() {
        step("1. Open the main application page, log in using correct credentials " +
                "and verify that username is displayed in the header and 'Log in' button is hidden", () -> {
            openLoginModalWindowAndLogin();

            assertThat(mainPage.getUsername()).hasText("Welcome " + USERNAME);
            assertThat(mainPage.getLoginButton()).isHidden();
        });

        step("2. Reload the page and check that the user is still logged in and 'Log in' button is still hidden", () -> {
            page.reload();

            assertThat(mainPage.getUsername()).isVisible();
            assertThat(mainPage.getUsername()).hasText("Welcome " + USERNAME);
            assertThat(mainPage.getLoginButton()).isHidden();
        });
    }

    @Step
    private void openLoginModalWindowAndLogin() {
        mainPage.navigate();
        mainPage.getLoginButton().click();

        mainPage.loginModal.login(USERNAME, PASSWORD);
    }
}
