import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.ProductPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;

public class ProductItemTests extends BaseTest {

    // Pages
    private ProductPage productPage;

    @BeforeEach
    public void setUpTest() {
        productPage = new ProductPage(page);
    }

    @Test
    public void test() {
        step("1. Open the main page, select first product in the product list " +
                "and check that information on the product page is matched with information from the product card", () -> {
            mainPage.navigate();

            // Need to wait until a list of products is loaded
            page.waitForCondition(() -> !mainPage.getDisplayedItemNames().isEmpty());

            var firstProductCardItem = mainPage.getAllDisplayedItems().get(0);
            var cardItemName = firstProductCardItem.getProductItemName().textContent();
            var cardItemPrice = firstProductCardItem.getProductItemPrice();
            var cardItemDescription = firstProductCardItem.getProductItemDescription();

            firstProductCardItem.getProductItemName().click();

            assertThat(productPage.getProductName()).hasText(cardItemName);
            assertThat(productPage.getProductPrice()).containsText(cardItemPrice);
            // the card item has a shortened description
            assertThat(productPage.getProductDescription()).containsText(cardItemDescription);
        });
    }
}
