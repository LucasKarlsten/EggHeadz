package se.nackademin.devops24.pingurl;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JUnitTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @LocalServerPort
    public int serverPort;

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
        page.navigate("http://localhost:" + serverPort);
        //deleteIfExists("Nackademin.se");
        //deleteIfExists("DN.se");
        page.fill("input[name='name']:visible", "Nackademin.se");
        page.fill("input[name='url']:visible", "https://www.Nackademin.se");
        page.click("input[type='submit']");
        page.fill("input[name='name']:visible", "DN.se");
        page.fill("input[name='url']:visible", "https://www.DN.se");
        page.click("input[type='submit']");
        page.click("table tr:nth-child(1) form button[type='submit']");
        page.click("table tr:nth-child(2) form button[type='submit']");
    }
    //private void deleteIfExists(String name) {
    //    Locator row = page.locator("table tr:has(td:has-text('" + name + "'))");
    //    if (row.isVisible()) {
    //        row.locator("form:has(button:has-text('Delete')) button[type='submit']").click();
    //        // Optional: wait for the row to disappear
    //        row.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
    //    }
    //}
}
