package se.nackademin.devops24.pingurl;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JUnitTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        context = browser.newContext(
                new Browser.NewContextOptions().setIgnoreHTTPSErrors(true)
        );
    }

    @BeforeEach
    void createPage() {
        page = context.newPage();
        // Set default timeout for actions (like fill, click) to 2 minutes
        page.setDefaultTimeout(120_000);
        // Set navigation timeout (for page.navigate) to 2 minutes
        page.setDefaultNavigationTimeout(120_000);
    }

    @AfterEach
    void closePage() {
        if (page != null) {
            page.close();
        }
    }

    @AfterAll
    void afterTest() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    void testAddUserandPing() {
        page.navigate("http://localhost:8080");
        page.fill("input[name='name']:visible", "Google.com");
        page.fill("input[name='url']:visible", "https://www.google.com");
        page.click("input[type='submit']");
        page.fill("input[name='name']:visible", "DN.se");
        page.fill("input[name='url']:visible", "https://www.DN.se");
        page.click("input[type='submit']");
        page.click("table tr:nth-child(1) form button[type='submit']");
        page.click("table tr:nth-child(2) form button[type='submit']");
    }
}
