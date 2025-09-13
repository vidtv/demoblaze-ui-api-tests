package ui;

import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import page.ProductDetailsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.ProductDetailsPage.PRODUCT_ADDED_ALERT_MESSAGE;

@Epic("Demoblaze")
@Feature("Product Details")
public class ProductDetailsTests extends BaseTest {
    private String sonyPhonePrice;

    // Pages
    private ProductDetailsPage productDetailsPage;

    // Test data
    private final String sonyPhoneName = "Sony xperia z5";

    @BeforeEach
    void setUpTest() {
        productDetailsPage = new ProductDetailsPage(page);
    }

    @Test
    @DisplayName("Check product details from catalog")
    @Description("Verify that clicking on a product in the catalog opens a page with its product card")
    void productsDetailsTest() {
        step("1. Open the main page, select '" + sonyPhoneName + "' product in the product list " +
                "and check that information on the product page is matched with information from the product card", () -> {
            mainPage.navigate();

            var samsungPhoneCardItem = mainPage.getProductByName(sonyPhoneName);
            sonyPhonePrice = samsungPhoneCardItem.getProductItemPrice();
            var samsungCardItemDescription = samsungPhoneCardItem.getProductItemDescription();

            samsungPhoneCardItem.getProductItemName().click();

            assertThat(productDetailsPage.getProductName()).hasText(sonyPhoneName);
            assertEquals(sonyPhonePrice, productDetailsPage.getProductPrice());
            // the product card item has a shortened description
            assertThat(productDetailsPage.getProductDescription()).containsText(samsungCardItemDescription);
        });
    }

    @Test
    @DisplayName("Add product to cart from product details page")
    @Description("Verify that a product is added to cart by clicking 'Add to cart' button on the product details page")
    void addProductToCartFromProductDetailsPageTest() {
        step("1. Open the main page, select '" + sonyPhoneName + "' product in the product list " +
                "and check that information on the product page is matched with information from the product card", () -> {
            mainPage.navigate();
            mainPage.getProductByName(sonyPhoneName).getProductItemName().click();
        });

        step("2. Click 'Add to cart' button, check that there's a correct alert about added product and accept the alert", () -> {
            page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());

            page.onDialog(dialog -> {
                assertEquals(PRODUCT_ADDED_ALERT_MESSAGE, dialog.message());
                dialog.accept();
            });
        });
    }
}
