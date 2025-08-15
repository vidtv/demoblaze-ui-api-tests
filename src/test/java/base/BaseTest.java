package base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import page.MainPage;

import java.util.List;

public abstract class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext browserContext;
    protected Page page;

    // Pages
    protected MainPage mainPage;

    @BeforeAll
    protected static void setUpBaseClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(List.of("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    protected void setUpBaseTest() {
        browserContext = browser.newContext();
        page = browserContext.newPage();

        mainPage = new MainPage(page);
    }

    @AfterEach
    protected void tearDownBaseTest() {
        if (browserContext != null) {
            browserContext.close();
        }

        if (page != null) {
            page.close();
        }
    }

    @AfterAll
    protected static void tearDownBaseClass() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}
