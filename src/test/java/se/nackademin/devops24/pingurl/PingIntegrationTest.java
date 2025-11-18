package se.nackademin.devops24.pingurl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import se.nackademin.devops24.pingurl.repository.MemoryURLRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PingIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    MemoryURLRepository repository;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();

        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withBody("MOCK-OK"))
        );

        int wmPort = wireMockServer.port();
        String mockUrl = "http://localhost:" + wmPort + "/";

        repository.save("TEST-MOCK", mockUrl);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void pingShouldCallMockServerAndReturnOk() {

        String pingEndpoint = "http://localhost:" + port + "/pingurl/ping";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "TEST-MOCK");

        ResponseEntity<String> response =
                restTemplate.postForEntity(pingEndpoint, form, String.class);

        System.out.println("STATUS = " + response.getStatusCode());
        System.out.println("BODY   = " + response.getBody());

        assertThat(response.getStatusCode().is2xxSuccessful())
            .as("Expected 2xx but got %s with body: %s",
                response.getStatusCode(), response.getBody())
            .isTrue();



        wireMockServer.verify(
                1,
                WireMock.getRequestedFor(WireMock.urlEqualTo("/"))
        );
    }
}

