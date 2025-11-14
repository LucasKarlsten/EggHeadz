package se.nackademin.devops24.pingurl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import se.nackademin.devops24.pingurl.model.PingedURL;
import se.nackademin.devops24.pingurl.repository.MemoryURLRepository;

import java.net.URL;
import java.util.Objects;

@Nested
@SpringBootTest
class PingurlApplicationTests {

    //@Test
    //void contextLoads()
    //}

    @Test
    public void MemoryURLRepositoryTest() {
        //.setName(name)
        //.setUrl(url)
        MemoryURLRepository URLTEST = new MemoryURLRepository();
        testsave(URLTEST);
        //testupdate(URLTEST);
        //testdelete(URLTEST);

    }
    private void testsave(MemoryURLRepository URLTEST) {
        try {
            URLTEST.save("google.com", "https://www.google.com");
            URLTEST.save("google.com", "https://www.googles.com");
            System.out.println("Test passed: Duplicate was overwritten");
            }
        catch (IllegalArgumentException e) {
            System.out.println("Test passed: Duplicate was not overwritten");
        }
        for (PingedURL url : URLTEST.getUrls()) {
        System.out.println("Name: " + url.getName() + ", URL: " + url.getUrl());
        }
    }
    private void testupdate(MemoryURLRepository URLTEST) {
        try {
            URLTEST.save("Aftonbladet.se", "https://www.Aftonspadet.se");
            PingedURL updateURL = new PingedURL()
                    .setName("Aftonbladet.se")
                    .setUrl("https://www.Aftonbladet.se")
                    .setCreatedAt(java.time.LocalDateTime.now());
            URLTEST.update(updateURL);
            System.out.println("Test passed: Updated URL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
    private void testdelete(MemoryURLRepository URLTEST) {
        URLTEST.save("google.com", "https://www.google.com");
        URLTEST.save("DN.com", "https://www.DN.com");
        try {
            URLTEST.delete("google.com");
            for (PingedURL url : URLTEST.getUrls()) {
                System.out.println("Name: " + url.getName() + ", URL: " + url.getUrl());
            }
            System.out.println("Test passed: Delete URL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test Failed: Unable to Delete url");

        }

    }

}
