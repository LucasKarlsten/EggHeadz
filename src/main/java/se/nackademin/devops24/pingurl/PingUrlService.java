package se.nackademin.devops24.pingurl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.nackademin.devops24.pingurl.model.PingedURL;
import se.nackademin.devops24.pingurl.repository.URLRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class PingUrlService {

    private static final Logger logger = LoggerFactory.getLogger(PingUrlService.class);

    private final URLRepository urlRepository;

    public PingUrlService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Collection<PingedURL> getPingUrls() {
        return urlRepository.getUrls();
    }

    public void addPingUrl(String url, String name) {
        urlRepository.save(name, url);
    }

    public void deleteUrlFromPing(String name) {
        urlRepository.delete(name);
    }

    // 🔹 används för "Ping nu"-knappen
    public void pingUrlNow(String name) {
        var pingedURLs = getPingUrls();
        for (var pingedURL : pingedURLs) {
            if (pingedURL.getName().equals(name)) {
                pingUrl(pingedURL);
                break;
            }
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void onesPerMinute() {
        var pingedURLs = getPingUrls();

        for (var pingedURL : pingedURLs) {
            pingUrl(pingedURL);
        }
    }

    private void pingUrl(PingedURL pingedURL) {
        try (var client = HttpClient.newBuilder().build()) {
            HttpRequest request = HttpRequest.newBuilder(new URI(pingedURL.getUrl())).GET().build();
            HttpResponse<Void> result = client.send(request, HttpResponse.BodyHandlers.discarding());

            // Check that the code is ok.
            if (result.statusCode() == 200) {
                pingedURL.setResult("success");
            } else {
                pingedURL.setResult("failure");
            }

            // 🔹 registrera senaste ping-tid
            pingedURL.setLastPinged(LocalDateTime.now());

            urlRepository.update(pingedURL);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.error("Ping failed", e);
            throw new RuntimeException(e);
        }
    }
}
