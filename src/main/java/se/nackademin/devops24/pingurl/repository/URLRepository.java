package se.nackademin.devops24.pingurl.repository;

import se.nackademin.devops24.pingurl.model.PingedURL;

import java.util.Collection;

public interface URLRepository {

    void save(String name, String url);
    Collection<PingedURL> getUrls();
    void update(PingedURL pingedURL);
    void delete(String name);
}
