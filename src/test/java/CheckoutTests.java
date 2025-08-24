import base.BaseTest;
import com.microsoft.playwright.Dialog;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import page.CartPage;
import page.ProductDetailsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static page.CartPage.THANK_YOU_FOR_YOUR_PURCHASE;

@Epic("Demoblaze")
@Feature("Checkout")
public class CheckoutTests extends BaseTest {

    // Pages
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    // Test data
    private final String nexusPhoneName = "Nexus 6";
    private final String iPhoneName = "Iphone 6 32gb";

    @BeforeEach
    void setUpTest() {
        productDetailsPage = new ProductDetailsPage(page);
        cartPage = new CartPage(page);

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(Dialog::accept);
    }

    @Test
    @DisplayName("Successful checkout with valid data")
    @Description("Verify Place Order flow with valid customer/payment information")
    void successfulCheckoutWithValidData() {
        step("1. Open the main page, select '" + nexusPhoneName + "' and add it to the cart", () -> {
            addProductToCartTestStep(nexusPhoneName);
        });

        step("2. Open the main page, select '" + iPhoneName + "' and add it to the cart", () -> {
            addProductToCartTestStep(iPhoneName);
        });

        step("3. Open the cart page, click 'Place Order' button, populate all fields in the modal window, click 'Purchase' button " +
                "and check opened alert about completed purchase", () -> {
            cartPage.navigate();
            page.waitForCondition(() -> cartPage.getAllCartItems().size() == 2);

            cartPage.getPlaceOrderButton().click();
            cartPage.placeOrderModal.populateOrderForm();

            assertThat(cartPage.getPurchaseCompleteAlert()).isVisible();
            assertThat(cartPage.getSuccessfulPurchaseCompleteIcon()).isVisible();
            assertThat(cartPage.getPurchaseCompleteMessage()).hasText(THANK_YOU_FOR_YOUR_PURCHASE);
        });
    }

    @Test
    @DisplayName("Validation of required fields of checkout form")
    void checkoutFormValidationTest() {

    }

    @Step
    private void addProductToCartTestStep(String productName) {
        mainPage.navigate();
        mainPage.selectProductByName(productName);
        page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());
    }
}
