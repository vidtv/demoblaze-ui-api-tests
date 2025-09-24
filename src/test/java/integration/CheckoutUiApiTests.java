package integration;

import base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ViewCartRequestBody;
import dto.ViewCartResponseBody;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.CartPage;
import page.ProductDetailsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.BASE_API_URL;

@Epic("Demoblaze")
@Feature("Checkout")
public class CheckoutUiApiTests extends BaseTest {
    private int deleteCartStatusCode;

    // Pages
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    // Test data
    private final int nexusPhoneId = 3;

    @BeforeEach
    void setUpTest() {
        productDetailsPage = new ProductDetailsPage(page);
        cartPage = new CartPage(page);
    }

    @Test
    void cartIsClearedAfterPurchase() {
        step("1. Open the main page of the shop and log in as a test user", () -> {
            mainPage.navigate();
            mainPage.loginAsTestUser();
        });

        step("2. Open product page for 'Nexus 6' product and add it to cart", () -> {
            productDetailsPage.openProductDetailsPage(nexusPhoneId);
            page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());
        });

        step("3. Open the cart page, click 'Place Order' button, populate required fields in the modal window, " +
                "click 'Purchase' button and check that /deletecart method returned 200 status code", () -> {
            cartPage.navigate();
            page.waitForCondition(() -> cartPage.getAllCartItems().size() == 1);

            page.onResponse(response -> {
                if (response.url().contains("deletecart")) {
                    deleteCartStatusCode = response.status();
                }
            });

            cartPage.getPlaceOrderButton().click();
            cartPage.placeOrderModal.modalLocator.locator("#name").fill("TestName");
            cartPage.placeOrderModal.modalLocator.locator("#card").fill("1111222233334444");

            page.waitForResponse("**/deletecart", () ->
                    cartPage.placeOrderModal.modalLocator.getByRole(BUTTON).getByText("Purchase").click()
            );
            assertThat(cartPage.getPurchaseCompleteAlert()).isVisible();

            assertEquals(200, deleteCartStatusCode);
        });

        step("4. Check that /viewcart returns empty cart content", () -> {
            var cookie = page.context().cookies().get(1).value;
            var viewCartRequestBody = new ObjectMapper().writeValueAsString(new ViewCartRequestBody(cookie));

            var viewCartResponseBody =
                    given()
                            .contentType(ContentType.JSON)
                            .body(viewCartRequestBody)
                            .when()
                            .post(BASE_API_URL + "/viewcart")
                            .then()
                            .statusCode(200)
                            .extract()
                            .asString();

            var viewCartResponse = new ObjectMapper().readValue(viewCartResponseBody, ViewCartResponseBody.class);

            var viewCartItemsList = viewCartResponse.items
                    .stream()
                    .toList();
            assertEquals(0, viewCartItemsList.size());
        });
    }
}
