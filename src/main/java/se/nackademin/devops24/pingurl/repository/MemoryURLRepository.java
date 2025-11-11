package se.nackademin.devops24.pingurl.repository;

import se.nackademin.devops24.pingurl.model.PingedURL;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryURLRepository implements URLRepository {

    private final Map<String, PingedURL> urls = new ConcurrentHashMap<>();

    @Override
    public void save(String name, String url) {
        PingedURL pingedURL = new PingedURL()
                .setName(name)
                .setUrl(url)
                .setCreatedAt(LocalDateTime.now());
        urls.put(name, pingedURL);
    }

    @Override
    public Collection<PingedURL> getUrls() {
        return urls.values();
    }

    @Override
    public void update(PingedURL pingedURL) {
        urls.put(pingedURL.getName(), pingedURL);
    }

    @Override
    public void delete(String name) {
        urls.remove(name);
    }
}
