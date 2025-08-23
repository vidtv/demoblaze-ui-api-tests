import base.BaseTest;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import page.CartPage;
import page.ProductPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.CartPage.THANK_YOU_FOR_YOUR_PURCHASE;
import static page.ProductPage.PRODUCT_ADDED_ALERT_MESSAGE;

public class AddProductToCartTest extends BaseTest {
    private String samsungPhonePrice;
    private String nokiaPhonePrice;

    // Pages
    private ProductPage productPage;
    private CartPage cartPage;

    // Test data
    private final String samsungPhoneName = "Samsung galaxy s6";
    private final String nokiaPhoneName = "Nokia lumia 1520";

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
    @DisplayName("Check product details from catalog. \n" +
            "Add product to cart from its product page. \n" +
            "Cart displays the added product. \n" +
            "Successful checkout with valid data")
    @Description("Verify that clicking on a product in the catalog opens a page with its product card " +
            "and information in the product card is matched with information from catalog item. \n" +
            "Verify that a product is added to cart by clicking 'Add to cart' button on the product details page. \n" +
            "Verify that the cart shows the product added from product page and its name and price are matched. \n" +
            "Verify Place Order flow with valid customer/payment information")
    public void test() {
        step("1. Open the main page, select '" + samsungPhoneName + "' product in the product list " +
                "and check that information on the product page is matched with information from the product card", () -> {
            mainPage.navigate();

            // Need to wait until a list of products is loaded
            page.waitForCondition(() -> !mainPage.getDisplayedItemNames().isEmpty());

            var samsungPhoneCardItem = mainPage.getProductByName(samsungPhoneName);
            samsungPhonePrice = samsungPhoneCardItem.getProductItemPrice().replace("$", "");
            var samsungCardItemDescription = samsungPhoneCardItem.getProductItemDescription();

            samsungPhoneCardItem.getProductItemName().click();

            assertThat(productPage.getProductName()).hasText(samsungPhoneName);
            assertThat(productPage.getProductPrice()).containsText(samsungPhonePrice);
            // the card item has a shortened description
            assertThat(productPage.getProductDescription()).containsText(samsungCardItemDescription);
        });

        step("2. Click 'Add to cart' button, check an alert about added product and accept the alert", () -> {
            page.waitForResponse("**/addtocart", () -> productPage.getAddToCartButton().click());
        });

        step("3. Open the cart page and check that the product name and price are the same " +
                "as in the product card item", () -> {
            cartPage.navigate();
            page.waitForCondition(() -> !cartPage.getAllCartItems().isEmpty());

            assertThat(cartPage.getAllCartItems().get(0).getName()).hasText(samsungPhoneName);
            // the price in cart is displayed without a currency sign for some reason
            assertEquals(cartPage.getAllCartItems().get(0).getPrice(), samsungPhonePrice);
        });

        step("4. Open the main page, select '" + nokiaPhoneName + "' and add it to the cart", () -> {
            mainPage.navigate();

            // Need to wait until a list of products is loaded
            page.waitForCondition(() -> !mainPage.getDisplayedItemNames().isEmpty());

            var nokiaPhoneCardItem = mainPage.getProductByName(nokiaPhoneName);
            nokiaPhonePrice = nokiaPhoneCardItem.getProductItemPrice().replace("$", "");
            nokiaPhoneCardItem.getProductItemName().click();

            page.waitForResponse("**/addtocart", () -> productPage.getAddToCartButton().click());
        });

        step("5. Open the cart page and check that both products are added to the cart " +
                "and total sum of the order is calculated correctly", () -> {
            page.waitForResponse("**/view", () -> cartPage.navigate());
            page.waitForCondition(() -> cartPage.getAllCartItems().size() == 2);

            var actualCartItemNames = cartPage.getAllCartItems()
                    .stream()
                    .map(item -> item.getName().textContent())
                    .toList();
            Assertions.assertThat(actualCartItemNames).containsExactlyInAnyOrder(samsungPhoneName, nokiaPhoneName);

            var expectedTotalSum = Integer.parseInt(samsungPhonePrice) + Integer.parseInt(nokiaPhonePrice);
            assertEquals(expectedTotalSum, Integer.parseInt(cartPage.getTotalOrderSum().textContent()));
        });

        step("6. Click 'Place Order' button, populate all fields in the modal window, click 'Purchase' button " +
                "and check opened alert about completed purchase", () -> {
            cartPage.getPlaceOrderButton().click();
            cartPage.placeOrderModal.populateOrderForm();

            assertThat(cartPage.getPurchaseCompleteAlert()).isVisible();
            assertThat(cartPage.getSuccessfulPurchaseCompleteIcon()).isVisible();
            assertThat(cartPage.getPurchaseCompleteMessage()).hasText(THANK_YOU_FOR_YOUR_PURCHASE);
        });
    }
}
