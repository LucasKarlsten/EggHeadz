package se.nackademin.devops24.pingurl;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.github.tomakehurst.wiremock.WireMockServer;

import se.nackademin.devops24.pingurl.model.PingedURL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PingIntegrationTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PingUrlService pingUrlService;

    @BeforeAll
    static void setupWiremock() {
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();
        configureFor("localhost", 9999);
    }

    @AfterAll
    static void stopWiremock() {
        wireMockServer.stop();
    }

    @Test
    void testPingFunctionWithWireMock() {
        // 1. WireMock ska svara 200 OK
        wireMockServer.stubFor(get(urlEqualTo("/ok"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("OK")));

        String testName = "test-url";
        String testUrl = "http://localhost:9999/ok";

        // 2. Registrera URL via POST /pingurl
        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "/pingurl?name=" + testName + "&url=" + testUrl,
                null,
                String.class
        );

        assertThat(createResponse.getStatusCode().is2xxSuccessful())
                .as("Skapa URL ska lyckas (redirect följs av TestRestTemplate)")
                .isTrue();

        // Kontrollera att den faktiskt finns registrerad
        Collection<PingedURL> allUrls = pingUrlService.getPingUrls();
        assertThat(allUrls)
                .extracting(PingedURL::getName)
                .contains(testName);

        // 3. Anropa "Ping nu" – POST /pingurl/ping
        ResponseEntity<String> pingResponse = restTemplate.postForEntity(
                "/pingurl/ping?name=" + testName,
                null,
                String.class
        );

        assertThat(pingResponse.getStatusCode().is2xxSuccessful())
                .as("Ping nu ska lyckas (redirect följs av TestRestTemplate)")
                .isTrue();

        // 4. Hämta URL från repository och verifiera resultat
        PingedURL result = pingUrlService.getPingUrls()
                .stream()
                .filter(url -> url.getName().equals(testName))
                .findFirst()
                .orElseThrow();

        assertThat(result.getLastPinged())
                .as("lastPinged ska ha satts")
                .isNotNull();

        assertThat(result.getResult())
                .as("result ska vara 'success'")
                .isEqualTo("success");

        // 5. Verifiera att WireMock-servern verkligen pingades
        wireMockServer.verify(getRequestedFor(urlEqualTo("/ok")));
    }
}
