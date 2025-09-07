package integration;

import base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EntriesResponse;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.BASE_API_URL;

@Epic("Demoblaze")
@Feature("Product Catalog")
public class ProductCatalogUiApiTests extends BaseTest {
    private List<String> displayedProductsList;

    @Test
    @DisplayName("Load product list via UI and API")
    @Description("Verify that the list of products displayed in the UI matches the list of products retrieved via the API")
    void loadProductListViaUIAndApiTest() {
        step("1. Open the main page of the store and retrieve a list of all displayed products on the page", () -> {
            mainPage.navigate();

            displayedProductsList = mainPage.getDisplayedItemNames();
        });

        step("2. Retrieve a list of all displayed products via REST API and check that they are the same", () -> {
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

            var apiProductsList = entriesResponse.items
                    .stream()
                    .map(item -> item.title)
                    .toList();

            assertEquals(displayedProductsList, apiProductsList);
        });
    }
}
