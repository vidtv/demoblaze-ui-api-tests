import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import page.CartPage;
import page.ProductPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.ProductPage.PRODUCT_ADDED_ALERT_MESSAGE;

@Epic("Demoblaze")
@Feature("Delete Products from Cart")
public class DeleteProductFromCartTest extends BaseTest {

    // Pages
    private ProductPage productPage;
    private CartPage cartPage;

    // Test data
    private final String samsungPhone = "Samsung galaxy s6";
    private final String nokiaPhone = "Nokia lumia 1520";

    @BeforeEach
    public void setUpTest() {
        productPage = new ProductPage(page);
        cartPage = new CartPage(page);

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(dialog -> {
            assertEquals(PRODUCT_ADDED_ALERT_MESSAGE, dialog.message());
            dialog.accept();
        });
    }

    @Test
    @DisplayName("")
    @Description("")
    public void test() {
        step("1. Open the main application page, select " + samsungPhone + " product from the products list " +
                "and add it to the cart", () -> {
            addProductToCartTestStep(samsungPhone);
        });

        step("2. Open the main application page, select " + nokiaPhone + " product from the products list " +
                "and add it to the cart", () -> {
            addProductToCartTestStep(nokiaPhone);
        });

        step("3. Open the cart page, click 'Delete' button for " + samsungPhone + " and check that cart item is no longer displayed " +
                "and total sum of order decreased accordingly", () -> {
            cartPage.navigate();
            var nokiaPhonePrice = cartPage.getCartItemByName(nokiaPhone).getPrice();

            cartPage.getCartItemByName(samsungPhone).getDeleteButton().click();

            assertThat(cartPage.getCartItemByName(samsungPhone).getSelf()).isHidden();
            assertThat(cartPage.getTotalOrderSum()).hasText(nokiaPhonePrice);
        });

        step("4. Click 'Delete' button for " + nokiaPhone + " and check that cart item is empty " +
                "and 'Total' field value is not displayed", () -> {
            cartPage.getCartItemByName(nokiaPhone).getDeleteButton().click();
            page.waitForCondition(() -> page.locator(".success").isHidden());

            assertThat(cartPage.getTotalOrderSum()).isHidden();
        });
    }

    @Step
    private void addProductToCartTestStep(String productName) {
        mainPage.navigate();
        // Need to wait until a list of products is loaded
        page.waitForCondition(() -> !mainPage.getDisplayedItemNames().isEmpty());

        mainPage.selectProductByName(productName);

        page.waitForResponse("**/addtocart", () -> productPage.getAddToCartButton().click());
    }
}
