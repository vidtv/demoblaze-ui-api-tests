import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static page.MainPage.*;

@Epic("Demoblaze")
@Story("UI Categories")
public class ProductCategoriesTest extends BaseTest {

    // Test data
    private final List<String> expectedPhoneNames = List.of("Samsung galaxy s6", "Nokia lumia 1520", "Nexus 6",
            "Samsung galaxy s7", "Iphone 6 32gb", "Sony xperia z5", "HTC One M9");
    private final List<String> expectedLaptopNames = List.of("Sony vaio i5", "Sony vaio i7\n", "MacBook air", "Dell i7 8gb",
            "2017 Dell 15.6 Inch", "MacBook Pro");
    private final List<String> expectedMonitorNames = List.of("Apple monitor 24", "ASUS Full HD");

    @Test
    @DisplayName("Navigation by categories (Phones/Laptops/Monitors)")
    @Description("Verify that each product category contains correct list of products")
    public void test() {
        step("1. Open the main application page, select 'Phones' category " +
                "and check that there are only phones displayed", () -> {
            mainPage.navigate();

            checkCategoryItemsStep(PHONES_CATEGORY, expectedPhoneNames);
        });

        step("2. Select 'Laptops' category and check that there are only laptops displayed", () -> {
            mainPage.navigate();

            checkCategoryItemsStep(LAPTOPS_CATEGORY, expectedLaptopNames);
        });

        step("3. Select 'Monitors' category and check that there are only monitors displayed", () -> {
            mainPage.navigate();

            checkCategoryItemsStep(MONITORS_CATEGORY, expectedMonitorNames);
        });
    }

    @Step
    private void checkCategoryItemsStep(String itemsCategory, List<String> expectedItemNames) {
        mainPage.getCategory(itemsCategory).click();

        page.waitForCondition(() -> expectedItemNames.size() == mainPage.getDisplayedItemNames().size());
        assertLinesMatch(expectedItemNames, mainPage.getDisplayedItemNames());
    }
}
