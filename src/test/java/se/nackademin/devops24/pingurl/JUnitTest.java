package se.nackademin.devops24.pingurl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JUnitTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @Autowired
    private PingUrlService service;

    @LocalServerPort
    public int serverPort;

    @BeforeAll
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );
        context = browser.newContext(
                new Browser.NewContextOptions().setIgnoreHTTPSErrors(true)
        );
    }

    @BeforeEach
    void createPage() {
        page = context.newPage();
        page.setDefaultTimeout(20_000);
        page.setDefaultNavigationTimeout(20_000);
    }

    @AfterEach
    void cleanRepoAndClosePage() {
        // Rensa alla registrerade URLs via servicen
        service.getPingUrls().forEach(url -> service.deleteUrlFromPing(url.getName()));

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
    void testAddUserAndPing() {
        String baseUrl = "http://localhost:" + serverPort;
        page.navigate(baseUrl);

        // Vänta tills formuläret för att lägga till URL är synligt
        page.waitForSelector("form[action='/pingurl'] input[name='name']:visible");
        page.waitForSelector("form[action='/pingurl'] input[name='url']:visible");

        Locator nameInput = page.locator("form[action='/pingurl'] input[name='name']:visible");
        Locator urlInput  = page.locator("form[action='/pingurl'] input[name='url']:visible");
        Locator submitBtn = page.locator("form[action='/pingurl'] input[type='submit']:visible");

        // Lägg till Nackademin.se
        nameInput.fill("Nackademin.se");
        urlInput.fill("https://www.nackademin.se");
        submitBtn.click();

        // Vänta tills formuläret är synligt igen
        page.waitForSelector("form[action='/pingurl'] input[name='name']:visible");
        page.waitForSelector("form[action='/pingurl'] input[name='url']:visible");

        // Lägg till DN.se
        nameInput.fill("DN.se");
        urlInput.fill("https://www.dn.se");
        submitBtn.click();

        // Vänta tills tabellen med rader finns
        page.waitForSelector("table tr");

        // Ping Nackademin.se
        page.click("tr:has(td:has-text('Nackademin.se')) form:has(button:has-text('Ping now')) button");

        // Ping DN.se
        page.click("tr:has(td:has-text('DN.se')) form:has(button:has-text('Ping now')) button");

        // (ev. assertions kan läggas här, men kravet är mest att flödet körs)
    }
}
