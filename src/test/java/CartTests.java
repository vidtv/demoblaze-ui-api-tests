import base.BaseTest;
import com.microsoft.playwright.Dialog;
import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import page.CartPage;
import page.ProductDetailsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Demoblaze")
@Feature("Cart")
public class CartTests extends BaseTest {
    private String samsungPhonePrice;
    private String nokiaPhonePrice;

    // Pages
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    // Test data
    private final String samsungPhoneName = "Samsung galaxy s6";
    private final String nokiaPhoneName = "Nokia lumia 1520";

    @BeforeEach
    void setUpTest() {
        productDetailsPage = new ProductDetailsPage(page);
        cartPage = new CartPage(page);

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(Dialog::accept);
    }

    @Test
    @DisplayName("Cart displays a single added product")
    @Description("Verify that the cart shows a single product added from product page and its name and price are matched")
    void singleProductInCartTest() {
        step("1. Open the main application page, select '" + samsungPhoneName + "' product in the product list " +
                "and add it to the cart on the product details page", () ->
                addProductToCartTestStep(samsungPhoneName)
        );

        step("2. Open the cart page and check that the product name and price are the same " +
                "as in the product card item", () -> {
            cartPage.navigate();
            page.waitForCondition(() -> !cartPage.getAllCartItems().isEmpty());

            assertThat(cartPage.getAllCartItems().get(0).getName()).hasText(samsungPhoneName);
            // the price in cart is displayed without a currency sign for some reason
            assertEquals(cartPage.getAllCartItems().get(0).getPrice(), samsungPhonePrice);
        });
    }

    @Test
    @DisplayName("Cart displays multiple added products")
    @Description("Verify that the cart shows multiple products added from product page and total sum is calculated correctly")
    void multipleProductsInCartTest() {
        step("1. Open the main page, select '" + samsungPhoneName + "' product in the product list " +
                "and add it to cart on the product details page", () ->
                addProductToCartTestStep(samsungPhoneName)
        );

        step("2. Open the main page, select '" + nokiaPhoneName + "' and add it to the cart", () ->
                addProductToCartTestStep(nokiaPhoneName)
        );

        step("3. Open the cart page and check that both products are added to the cart " +
                "and total sum of the order is calculated correctly", () -> {
            cartPage.navigate();
            page.waitForCondition(() -> cartPage.getAllCartItems().size() == 2);

            var actualCartItemNames = cartPage.getAllCartItems()
                    .stream()
                    .map(item -> item.getName().textContent())
                    .toList();
            Assertions.assertThat(actualCartItemNames).containsExactlyInAnyOrder(samsungPhoneName, nokiaPhoneName);

            var expectedTotalSum = Integer.parseInt(samsungPhonePrice) + Integer.parseInt(nokiaPhonePrice);
            assertEquals(expectedTotalSum, Integer.parseInt(cartPage.getTotalOrderSum().textContent()));
        });
    }

    @Test
    @DisplayName("Remove product from cart and update total")
    @Description("Verify that after removing a product from the cart the product row disappears and total is recalculated")
    void removeProductFromCartTest() {
        step("1. Open the main application page, select " + samsungPhoneName + " product from the products list " +
                "and add it to the cart", () -> {
            addProductToCartTestStep(samsungPhoneName);
        });

        step("2. Open the main application page, select " + nokiaPhoneName + " product from the products list " +
                "and add it to the cart", () -> {
            addProductToCartTestStep(nokiaPhoneName);
        });

        step("3. Open the cart page, click 'Delete' button for " + samsungPhoneName + " and check that cart item is no longer displayed " +
                "and total sum of order decreased accordingly", () -> {
            cartPage.navigate();
            cartPage.getCartItemByName(samsungPhoneName).getDeleteButton().click();

            assertThat(cartPage.getCartItemByName(samsungPhoneName).getSelf()).isHidden();
            // the expected total sum is equal to the price of Nokia phone after Samsung phone is deleted
            assertThat(cartPage.getTotalOrderSum()).hasText(nokiaPhonePrice);
        });

        step("4. Click 'Delete' button for " + nokiaPhoneName + " and check that cart item is empty " +
                "and 'Total' field value is not displayed", () -> {
            cartPage.getCartItemByName(nokiaPhoneName).getDeleteButton().click();
            page.waitForCondition(() -> page.locator(".success").isHidden());

            assertThat(cartPage.getTotalOrderSum()).isHidden();
        });
    }

    @Step
    private void addProductToCartTestStep(String productName) {
        mainPage.navigate();

        if (productName.equals(samsungPhoneName)) {
            samsungPhonePrice = mainPage.getProductByName(samsungPhoneName).getProductItemPrice();
        } else {
            nokiaPhonePrice = mainPage.getProductByName(nokiaPhoneName).getProductItemPrice();
        }
        mainPage.selectProductByName(productName);

        page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());
    }
}
