package se.nackademin.devops24.pingurl.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import se.nackademin.devops24.pingurl.model.PingedURL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository  // <--- IMPORTANT! Makes @PostConstruct run
public class MemoryURLRepository implements URLRepository {

    private final Map<String, PingedURL> urls = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private final File jsonFile = new File("data.json");

    @PostConstruct
    public void init() {
        ensureJsonFileExists();
        loadFromFile();
    }

    private void ensureJsonFileExists() {
        if (!jsonFile.exists()) {
            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write("[]");
                System.out.println("Created new data.json");
            } catch (IOException e) {
                throw new RuntimeException("Could not create data.json", e);
            }
        }
    }

    private void loadFromFile() {
        try {
            var list = mapper.readValue(jsonFile, new TypeReference<Collection<PingedURL>>() {});
            list.forEach(u -> urls.put(u.getName(), u));
            System.out.println("Loaded " + urls.size() + " URLs from JSON");
        } catch (Exception e) {
            System.err.println("Failed to load data.json: " + e.getMessage());
        }
    }

    private synchronized void saveToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, urls.values());
        } catch (IOException e) {
            System.err.println("Failed to save data.json: " + e.getMessage());
        }
    }

    @Override
    public void save(String name, String url) {
        if (urls.containsKey(name)) {
            throw new IllegalArgumentException(name + " already exists!");
        }

        PingedURL pingedURL = new PingedURL()
                .setName(name)
                .setUrl(url)
                .setCreatedAt(LocalDateTime.now())
                .setLastPinged(null);

        urls.put(name, pingedURL);
        saveToFile();
    }

    @Override
    public Collection<PingedURL> getUrls() {
        return urls.values();
    }

    @Override
    public void update(PingedURL pingedURL) {
        urls.put(pingedURL.getName(), pingedURL);
        saveToFile();
    }

    @Override
    public void delete(String name) {
        urls.remove(name);
        saveToFile();
    }
}
