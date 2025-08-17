import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Epic("Demoblaze")
@Feature("Product Catalog")
public class ProductListPaginationTest extends BaseTest {

    @Test
    @DisplayName("Pagination of a products list")
    @Description("Verify that pagination of a products list works correctly (the products are different on different pages)")
    public void test() {
        step("1. Open the main application page and check that 'Next' button is displayed", () -> {
            mainPage.navigate();

            assertThat(mainPage.getNextPageButton()).isVisible();
            // Need to wait until a list of products is loaded to have a successful click of 'Next' button
            page.waitForCondition(() -> !mainPage.getDisplayedItemNames().isEmpty());
        });

        step("2. Click 'Next' button and check that 'Previous' button is now displayed " +
                "and 'Next' button is hidden", () -> {
            mainPage.getNextPageButton().click();

            assertThat(mainPage.getPreviousPageButton()).isVisible();
            assertThat(mainPage.getNextPageButton()).isHidden();
        });

        step("3. Check that product lists on the current and previous pages are different", () -> {
            var currentPageProductNames = mainPage.getDisplayedItemNames();

            mainPage.getPreviousPageButton().click();
            // Need to make sure that switching to the previous page happened
            assertThat(mainPage.getNextPageButton()).isVisible();
            var previousPageProductNames = mainPage.getDisplayedItemNames();

            assertNotEquals(currentPageProductNames, previousPageProductNames);
        });
    }
}
