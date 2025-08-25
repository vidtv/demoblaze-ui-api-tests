import base.BaseTest;
import com.microsoft.playwright.Dialog;
import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import page.CartPage;
import page.ProductDetailsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static utils.Constants.PASSWORD;
import static utils.Constants.USERNAME;

@Epic("Demoblaze")
@Feature("Session Persistence")
public class SessionPersistenceTests extends BaseTest {

    // Pages
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    @BeforeEach
    void setUpTest() {
        productDetailsPage = new ProductDetailsPage(page);
        cartPage = new CartPage(page);

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(Dialog::accept);
    }

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

    @Test
    @DisplayName("Cart state persists after page reload")
    @Description("Verify that products added to the cart remain visible after refreshing the page")
    void cartStatePersistsAfterReload() {
        var samsungPhoneName = "Samsung galaxy s6";
        var nokiaPhoneName = "Nokia lumia 1520";

        step("1. Open the main application page, select '" + samsungPhoneName + "' and add it to the cart", () -> {
            addProductToCartTestStep(samsungPhoneName);
        });

        step("2. Open the main application page, select '" + nokiaPhoneName + "' and add it to the cart", () -> {
            addProductToCartTestStep(nokiaPhoneName);
        });

        step("3. Open the cart page, reload it and check that items and total sum remain the same", () -> {
            cartPage.navigate();
            page.waitForCondition(() -> cartPage.getAllCartItems().size() == 2);
            var initialTotalSum = cartPage.getTotalOrderSum().textContent();

            page.reload();
            page.waitForCondition(() -> cartPage.getAllCartItems().size() == 2);

            var actualCartItemNames = cartPage.getAllCartItems()
                    .stream()
                    .map(item -> item.getName().textContent())
                    .toList();
            Assertions.assertThat(actualCartItemNames).containsExactlyInAnyOrder(samsungPhoneName, nokiaPhoneName);
            assertThat(cartPage.getTotalOrderSum()).hasText(initialTotalSum);
        });
    }

    @Step
    private void openLoginModalWindowAndLogin() {
        mainPage.navigate();
        mainPage.getLoginButton().click();

        mainPage.loginModal.login(USERNAME, PASSWORD);
    }

    @Step
    private void addProductToCartTestStep(String productName) {
        mainPage.navigate();
        mainPage.selectProductByName(productName);
        page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());
    }
}
