import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.CartPage;
import page.ProductPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.ProductPage.PRODUCT_ADDED_ALERT_MESSAGE;

public class AddProductToCartTest extends BaseTest {
    private String cardItemName;
    private String cardItemPrice;

    // Pages
    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeEach
    public void setUpTest() {
        productPage = new ProductPage(page);
        cartPage = new CartPage(page);
    }

    @Test
    public void test() {
        step("1. Open the main page, select first product in the product list " +
                "and check that information on the product page is matched with information from the product card", () -> {
            mainPage.navigate();

            // Need to wait until a list of products is loaded
            page.waitForCondition(() -> !mainPage.getDisplayedItemNames().isEmpty());

            var firstProductCardItem = mainPage.getAllDisplayedItems().get(0);
            cardItemName = firstProductCardItem.getProductItemName().textContent();
            cardItemPrice = firstProductCardItem.getProductItemPrice();
            var cardItemDescription = firstProductCardItem.getProductItemDescription();

            firstProductCardItem.getProductItemName().click();

            assertThat(productPage.getProductName()).hasText(cardItemName);
            assertThat(productPage.getProductPrice()).containsText(cardItemPrice);
            // the card item has a shortened description
            assertThat(productPage.getProductDescription()).containsText(cardItemDescription);
        });

        step("2. Click 'Add to cart' button, check an alert about added product and accept the alert", () -> {
            productPage.getAddToCartButton().click();

            page.onDialog(dialog -> {
                assertEquals(PRODUCT_ADDED_ALERT_MESSAGE, dialog.message());
                dialog.accept();
            });
        });

        step("3. Open the cart page and check that the product name and price are the same " +
                "as in the product card item", () -> {
            cartPage.navigate();

            assertThat(cartPage.getAllCartItems().get(0).getName()).hasText(cardItemName);
            // the price in cart is displayed without a currency sign for some reason
            assertEquals(cartPage.getAllCartItems().get(0).getPrice(), cardItemPrice.replace("$", ""));
        });
    }
}
