package integration;

import base.BaseTest;
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
    private List<String> apiProductsList;
    private List<String> apiFilteredProductsList;

    //@Test
    @DisplayName("Load product list via UI and API")
    @Description("Verify that the list of products displayed in the UI matches the list of products retrieved via the API")
    void loadProductListViaUIAndApiTest() {
        step("1. Retrieve a list of all displayed products via REST API", () -> {
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

            apiProductsList = entriesResponse.items
                    .stream()
                    .map(item -> item.title)
                    .toList();
        });

        step("2. Open the main page of the store and retrieve a list of all displayed products on the page " +
                "and check that it's the same as the list retrieved via REST API", () -> {
            mainPage.navigate();
            var displayedProductsList = mainPage.getDisplayedItemNames();

            assertEquals(apiProductsList, displayedProductsList);
        });
    }

    //@Test
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

        step("1. Retrieve product details for 'Samsung Galaxy S6' phone (id = 1)", () -> {
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
            mainPage.selectProductByName(samsungPhoneName);

            assertThat(productDetailsPage.getProductName()).hasText(samsungPhoneInformation.title);
            assertEquals(String.valueOf(samsungPhoneInformation.getItemPrice()), productDetailsPage.getProductPrice());
            assertThat(productDetailsPage.getProductDescription()).hasText(samsungPhoneInformation.desc);
        });
    }
}
