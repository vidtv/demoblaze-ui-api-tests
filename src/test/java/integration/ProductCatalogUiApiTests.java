package integration;

import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EntriesResponse;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.ProductDetailsPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.MainPage.PHONES_CATEGORY;
import static utils.Constants.BASE_API_URL;

@Epic("Demoblaze")
@Feature("Product Catalog")
public class ProductCatalogUiApiTests extends BaseTest {
    private List<String> apiStartPageProductsList;
    private List<String> apiFilteredProductsList;
    private List<String> apiFirstPageProductsList;
    private List<String> apiSecondPageProductsList;

    @Test
    @DisplayName("Load product list via UI and API")
    @Description("Verify that the list of products displayed in the UI matches the list of products retrieved via the API")
    void loadProductListViaUIAndApiTest() {
        step("1. Retrieve a list of all displayed products via REST API", () -> {
            apiStartPageProductsList = retrieveStartPageProductsList();
        });

        step("2. Open the main page of the store and retrieve a list of all displayed products on the page " +
                "and check that it's the same as the list retrieved via REST API", () -> {
            mainPage.navigate();
            var displayedProductsList = mainPage.getDisplayedItemNames();

            assertEquals(apiStartPageProductsList, displayedProductsList);
        });
    }

    @Test
    @DisplayName("Filter products by category via REST API and UI")
    @Description("Verify that product filtering by category (Phones, Laptops, Monitors) in UI matches the data from API")
    void productsFiltrationByUiAndApiTest() {
        step("1. Retrieve a filtered list by 'Phones' category via REST API", () -> {
            var filteredByCategoryProductsResponseJson =
                    given()
                            .contentType(ContentType.JSON)
                            .body("{\"cat\":\"phone\"}")
                    .when()
                            .post(BASE_API_URL + "/bycat")
                    .then()
                            .statusCode(200)
                            .extract()
                            .response()
                            .asString();

            var filteredProductResponse = new ObjectMapper().readValue(filteredByCategoryProductsResponseJson,
                    EntriesResponse.class);

            apiFilteredProductsList = filteredProductResponse.items
                    .stream()
                    .map(item -> item.title)
                    .toList();
        });

        step("2. Open the main page of the store, filter products by 'Phones' category " +
                "and verify that the filtered product is the same as the filtered products list retrieved via REST API", () -> {
            mainPage.navigate();
            mainPage.getCategory(PHONES_CATEGORY).click();
            page.waitForCondition(() -> mainPage.getAllDisplayedItems().size() == apiFilteredProductsList.size());

            var displayedPhonesList = mainPage.getDisplayedItemNames();

            assertEquals(apiFilteredProductsList, displayedPhonesList);
        });
    }

    @Test
    @DisplayName("Load product details via UI and API")
    @Description("Verify that product details displayed in the UI match the product details retrieved via the API")
    void productDetailsViaUiAndApiTest() {
        var productDetailsPage = new ProductDetailsPage(page);
        var samsungPhoneName = "Samsung galaxy s6";

        step("Retrieve product details for 'Samsung Galaxy S6' phone (id = 1), open the main page of the store, " +
                "select 'Samsung galaxy s6' product and check that product details displayed in the UI " +
                "match the product details retrieved via the API", () -> {
            var samsungPhoneInformationJson =
                    given()
                            .contentType(ContentType.JSON)
                            .body("{\"id\":\"1\"}")
                    .when()
                            .post(BASE_API_URL + "/view")
                    .then()
                            .statusCode(200)
                            .extract()
                            .response()
                            .asString();

            var samsungPhoneInformation = new ObjectMapper().readValue(samsungPhoneInformationJson,
                    EntriesResponse.Item.class);

            mainPage.navigate();
            mainPage.getProductByName(samsungPhoneName).getProductItemName().click();

            assertThat(productDetailsPage.getProductName()).hasText(samsungPhoneInformation.title);
            assertEquals(String.valueOf(samsungPhoneInformation.getItemPrice()), productDetailsPage.getProductPrice());
            assertThat(productDetailsPage.getProductDescription()).hasText(samsungPhoneInformation.desc);
        });
    }

    @Test
    @DisplayName("Products lists pagination via UI and API")
    @Description("Verify that pagination of products list in UI works correctly and matches the data retrieved via the API")
    void productsPaginationViaUiAndApiTest() {
        step("1. Retrieve products for the start, first and second pages of the products catalog via REST API", () -> {
            // the lists on the start page and on the first page are different
            apiStartPageProductsList = retrieveStartPageProductsList();
            apiFirstPageProductsList = retrievePageProductsList("{\"id\":\"1\"}");
            apiSecondPageProductsList = retrievePageProductsList("{\"id\":\"9\"}");
        });

        step("2. Open the main page of the store and verify that products list on the start page " +
                "matches with the list retrieved by REST API", () -> {
            mainPage.navigate();
            var startPageProducts = mainPage.getDisplayedItemNames();

            assertEquals(apiStartPageProductsList, startPageProducts);
        });

        step("3. Click 'Next' button and verify that products list on the second page " +
                "matches with the list retrieved by REST API", () -> {
            page.waitForResponse("**/pagination", () -> mainPage.getNextPageButton().click());
            var secondPageProducts = mainPage.getDisplayedItemNames();

            assertEquals(apiSecondPageProductsList, secondPageProducts);
        });

        step("4. Click 'Previous' button and verify that products on the first page " +
                "matches with the list retrieved by REST API", () -> {
            page.waitForResponse("**/pagination", () -> mainPage.getPreviousPageButton().click());
            // for more stable test behaviour
            page.waitForCondition(() -> mainPage.getNextPageButton().isVisible());
            var firstPageProducts = mainPage.getDisplayedItemNames();

            assertEquals(apiFirstPageProductsList, firstPageProducts);
        });
    }

    /**
     * Retrieve a list of all products from the start page via REST API.
     *
     * @return list of product names displayed on the start page
     * @throws JsonProcessingException if there's an error processing JSON
     */
    private List<String> retrieveStartPageProductsList() throws JsonProcessingException {
        var entriesResponseBody = given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_API_URL + "/entries")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        var entriesResponse = new ObjectMapper().readValue(entriesResponseBody, EntriesResponse.class);

        return entriesResponse.items
                .stream()
                .map(item -> item.title)
                .toList();
    }

    /**
     * Retrieve a list of products for a specific page via REST API.
     *
     * @param  jsonRequestBody the JSON request body
     * @return list of product names displayed on the specified page
     * @throws JsonProcessingException if there's an error processing JSON
     */
    private List<String> retrievePageProductsList(String jsonRequestBody) throws JsonProcessingException {
        var firstPageProductsResponseJson =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonRequestBody)
                .when()
                        .post(BASE_API_URL + "/pagination")
                .then()
                        .statusCode(200)
                        .extract()
                        .response()
                        .asString();

        var firstPageProductsResponse = new ObjectMapper().readValue(firstPageProductsResponseJson,
                EntriesResponse.class);

        return firstPageProductsResponse.items
                .stream()
                .map(item -> item.title)
                .toList();
    }
}
