package integration;

import base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Dialog;
import dto.ViewCartRequestBody;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import page.ProductDetailsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;

@Epic("Demoblaze")
@Feature("Shopping Cart")
public class ShoppingCartUiApiTests extends BaseTest {
    private int addToCartStatusCode;
    private String cookie;

    // Pages
    private ProductDetailsPage productDetailsPage;

    // Test data
    private final String nexusPhoneName = "Nexus 6";
    private final int nexusPhoneId = 3;

    @BeforeEach
    void setUpTest() {
        productDetailsPage = new ProductDetailsPage(page);

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(Dialog::accept);
    }

    @Test
    @DisplayName("Add product to cart via UI and verify via API")
    @Description("Verify that a product added to the cart via the UI is present in the cart when retrieved via the API")
    void addToCartViaApiAndCheckInUiTest() {
        step("1. Open the main page of the shop and log in as a test user", () -> {
            mainPage.navigate();
            mainPage.getLoginButton().click();

            assertThat(mainPage.loginModal.getLoginModalTitle()).isVisible();
            mainPage.loginModal.login(USERNAME, PASSWORD);

            assertThat(mainPage.getUsername()).hasText("Welcome " + USERNAME);
            cookie = page.context().cookies().get(1).value;
        });

        step("2. Open product page for 'Nexus 6' product, add it to cart " +
                "and check that /addtocart response has status code 200", () -> {
            mainPage.selectProductByName(nexusPhoneName);

            page.onResponse(response -> {
                if (response.url().contains("addtocart")) {
                    addToCartStatusCode = response.status();
                }
            });

            page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());

            assertEquals(200, addToCartStatusCode);
        });

        step("3. Retrieve list of added products via REST API and check that it contains product with id = " + nexusPhoneId, () -> {
            var viewCartJsonBody = new ObjectMapper().writeValueAsString(new ViewCartRequestBody(cookie));

            given()
                    .contentType(ContentType.JSON)
                    .body(viewCartJsonBody)
            .when()
                    .post(BASE_API_URL + "/viewcart")
            .then()
                    .statusCode(200)
                    .body("Items[0].prod_id", equalTo(nexusPhoneId));
        });
    }
}
