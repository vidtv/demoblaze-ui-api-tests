package integration;

import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Dialog;
import dto.ViewCartRequestBody;
import dto.ViewCartResponseBody;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import page.CartPage;
import page.ProductDetailsPage;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.BASE_API_URL;

@Epic("Demoblaze")
@Feature("Shopping Cart")
public class ShoppingCartUiApiTests extends BaseTest {
    private int addToCartStatusCode;
    private String nexusPhoneViewCartId;

    // Pages
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    // Test data
    private final int nexusPhoneId = 3;
    private final List<Integer> productsIdList = List.of(nexusPhoneId, 2, 4);

    @BeforeEach
    void setUpTest() {
        productDetailsPage = new ProductDetailsPage(page);
        cartPage = new CartPage(page);

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(Dialog::accept);
    }

    @AfterEach
    void tearDownTest() {
        step("Retrieve all added products to cart and delete them by REST API", () -> {
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

            var viewCartProdIdList = viewCartResponse.items
                    .stream()
                    .map(item -> item.id)
                    .toList();

            for (var productId : viewCartProdIdList) {
                deleteProductFromCartViaApi(productId);
            }
        });
    }

    @Test
    @DisplayName("Add product to cart via UI and delete it via API")
    @Description("Verify that a product added to the cart via the UI is present in the cart when retrieved via the API " +
            "and deleted via API properly")
    void addToCartViaUiAndDeleteViaApiTest() {
        step("1. Open the main page of the shop and log in as a test user", () -> {
            mainPage.navigate();
            mainPage.loginAsTestUser();
        });

        step("2. Open product page for 'Nexus 6' product, add it to cart " +
                "and check that /addtocart response has status code 200", () -> {
            page.onResponse(response -> {
                if (response.url().contains("addtocart")) {
                    addToCartStatusCode = response.status();
                }
            });

            addProductToCartTestStep(nexusPhoneId);

            assertEquals(200, addToCartStatusCode);
        });

        step("3. Retrieve list of added products via REST API and check that it contains product with id = " + nexusPhoneId, () -> {
            var cookie = page.context().cookies().get(1).value;
            var viewCartJsonBody = new ObjectMapper().writeValueAsString(new ViewCartRequestBody(cookie));

            nexusPhoneViewCartId = given()
                    .contentType(ContentType.JSON)
                    .body(viewCartJsonBody)
            .when()
                    .post(BASE_API_URL + "/viewcart")
            .then()
                    .statusCode(200)
                    .body("Items[0].prod_id", equalTo(nexusPhoneId))
                    .extract()
                    .jsonPath()
                    .getString("Items[0].id");
        });

        step("4. Remove the added product from the cart via REST API", () ->
                deleteProductFromCartViaApi(nexusPhoneViewCartId)
        );

        step("5. Open the cart and verify that it's empty", () -> {
            cartPage.navigate();

            assertEquals(0, cartPage.getAllCartItems().size());
        });
    }

    @Test
    @DisplayName("Add multiple products to cart via UI and verify via API")
    @Description("Verify that multiple products added to the cart via the UI are present in the cart when retrieved via the API")
    void addMultipleProductsViaUiAndVerifyViaApi() {
        step("1. Open the main page of the shop and log in as a test user", () -> {
            mainPage.navigate();
            mainPage.loginAsTestUser();
        });

        step("2. Open product details page for all of 'Nexus 6', 'Nokia lumia 1520' (id = 2) " +
                "and 'Samsung galaxy s7' (id = 4) products and add each of them to the cart", () -> {
            for (var productId : productsIdList) {
                addProductToCartTestStep(productId);
            }
        });

        step("3. Retrieve cart content via REST API and verify that API response contains prod_ids of the same products " +
                        "that were added via UI",
                this::verifyProdIdListsFromUiAndApi
        );
    }

    @Test
    @DisplayName("Cart persistence after relogin")
    @Description("Verify that products added to the cart are still present in the cart after user logs out and logs in again")
    void cartPersistsAfterReloginTest() {
        step("1. Open the main page of the shop and log in as a test user", () -> {
            mainPage.navigate();
            mainPage.loginAsTestUser();
        });

        step("2. Open product details page for all of 'Nexus 6', 'Nokia lumia 1520' (id = 2) " +
                "and 'Samsung galaxy s7' (id = 4) products and add each of them to the cart", () -> {
            for (var productId : productsIdList) {
                addProductToCartTestStep(productId);
            }
        });

        step("3. Log out from the shop", () -> {
            mainPage.getLogoutButton().click();
            page.waitForCondition(() -> mainPage.getLoginButton().isVisible());
        });

        step("4. Log in as a test user again and open the cart page", () -> {
            mainPage.loginAsTestUser();
        });

        step("5. Retrieve cart content for the current session via REST API and verify that API response " +
                        "contains prod_ids of the same products that were added via UI",
                this::verifyProdIdListsFromUiAndApi
        );
    }

    @Step
    private void addProductToCartTestStep(int productId) {
        productDetailsPage.openProductDetailsPage(productId);
        page.waitForResponse("**/addtocart", () -> productDetailsPage.getAddToCartButton().click());
    }

    @Step
    private void deleteProductFromCartViaApi(String viewCartId) {
        given()
                .contentType(ContentType.JSON)
                .body("{\"id\":\"" + viewCartId + "\"}")
        .when()
                .post(BASE_API_URL + "/deleteitem")
        .then()
                .statusCode(200);
    }

    /**
     * Retrieve cart content via REST API
     * and verify that API response contains prod_ids of the same products
     * that were added via UI.
     *
     * @throws JsonProcessingException if there is an error processing JSON
     */
    private void verifyProdIdListsFromUiAndApi() throws JsonProcessingException {
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

        var viewCartIdList = viewCartResponse.items
                .stream()
                .map(item -> item.prod_id)
                .toList();
        Assertions.assertThat(viewCartIdList).hasSameElementsAs(productsIdList);
    }
}
