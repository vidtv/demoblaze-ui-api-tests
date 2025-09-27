package ui;

import base.BaseTest;
import com.microsoft.playwright.Dialog;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import page.CartPage;
import page.ProductDetailsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.CartPage.PLEASE_FILL_OUT_NAME_AND_CREDIT_CARD;
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
        step("1. Open the main page, select '" + nexusPhoneName + "' and add it to the cart", () ->
                addProductToCartTestStep(nexusPhoneName)
        );

        step("2. Open the main page, select '" + iPhoneName + "' and add it to the cart", () ->
                addProductToCartTestStep(iPhoneName)
        );

        step("3. Open the cart page, click 'Place Order' button, populate required fields in the modal window, " +
                "click 'Purchase' button and check opened alert about completed purchase", () -> {
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
    @Description("Verify that alert appears in case of empty required fields on purchase form")
    void checkoutFormValidationTest() {
        step("1. Open the main page, select '" + nexusPhoneName + "' and add it to the cart", () ->
                addProductToCartTestStep(nexusPhoneName)
        );

        step("2. Open the cart page, click 'Place Order' button, leave all fields empty in the modal window, " +
                "click 'Purchase' button and check alert message about missing required fields", () -> {
            cartPage.navigate();
            cartPage.getPlaceOrderButton().click();

            checkRequiredFieldsAlertStep();
        });

        step("3. Populate 'Name' field, click 'Purchase' button " +
                "and check that alert message about missing required fields still appears", () -> {
            cartPage.placeOrderModal.getNameInput().fill("TestName");

            checkRequiredFieldsAlertStep();
        });

        step("4. Clear 'Name' field, populate 'Credit Card' field, click 'Purchase' button " +
                "and check that alert message about missing required fields still appears", () -> {
            cartPage.placeOrderModal.getNameInput().clear();
            cartPage.placeOrderModal.getCreditCardInput().fill("11112222");

            checkRequiredFieldsAlertStep();
        });
    }

    @Step
    private void addProductToCartTestStep(String productName) {
        mainPage.navigate();
        mainPage.getProductByName(productName).getProductItemName().click();
        page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());
    }

    @Step
    private void checkRequiredFieldsAlertStep() {
        cartPage.placeOrderModal.getPurchaseButton().click();

        page.onDialog(dialog -> assertEquals(PLEASE_FILL_OUT_NAME_AND_CREDIT_CARD, dialog.message()));
    }
}
