package integration;

import base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Dialog;
import dto.LoginBody;
import dto.SignUpBody;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.util.Base64.getEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.modal.SignUpModal.SIGN_UP_MODAL_TITLE;
import static utils.Constants.BASE_API_URL;

@Epic("Demoblaze")
@Feature("User Authentication and Registration")
public class AuthIntegrationTests extends BaseTest {
    private String signUpRequestBody;
    private String encodedPassword;

    @BeforeEach
    void setUpTest() {
        page.onRequest(request -> {
            if (request.url().contains("signup")) {
                signUpRequestBody = request.postData();
            }
        });

        // to handle alert messages that appear after adding a product to cart
        page.onDialog(Dialog::accept);
    }

    @Test
    void shouldCreateNewUserViaUiAndVerifyInApi() {
        var username = UUID.randomUUID() + "@playwright.com";
        var password = "testpass";

        step("1. Open the main application page, click 'Sign up' button and check that modal window is opened", () -> {
            mainPage.navigate();
            mainPage.getSignUpButton().click();

            assertThat(mainPage.signUpModal.getModalTitle()).hasText(SIGN_UP_MODAL_TITLE);
        });

        step("2. Populate username and password inputs with correct values and click 'Sign up' button", () -> {
            mainPage.signUpModal.getUsernameInput().fill(username);
            mainPage.signUpModal.getPasswordInput().fill(password);
            mainPage.signUpModal.getSignUpButton().click();
        });

        step("3. Verify that the request to 'signup' endpoint contains correct username and password", () -> {
            try {
                var signUpBody = new ObjectMapper().readValue(signUpRequestBody, SignUpBody.class);

                encodedPassword = getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));

                assertEquals(signUpBody.username, username, "Sign up request contains proper username");
                assertEquals(signUpBody.password, encodedPassword, "Sign up request contains Base64 encoded password");
            } catch (JSONException e) {
                throw new RuntimeException("Something wrong with request JSON: " + e.getMessage());
            }
        });

        step("4. Try to log in with a created user credentials via REST API and check that response code is 200", () -> {
            var loginBody = new LoginBody(username, encodedPassword);
            var loginRequestBody = new ObjectMapper().writeValueAsString(loginBody);

            given()
                    .contentType("application/json")
                    .body(loginRequestBody)
            .when()
                    .post(BASE_API_URL + "/login")
            .then()
                    .statusCode(200);
        });
    }
}
