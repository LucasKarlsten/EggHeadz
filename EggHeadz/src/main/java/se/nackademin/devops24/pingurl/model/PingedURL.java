package se.nackademin.devops24.pingurl.model;

import java.time.LocalDateTime;

public class PingedURL {
    private String name;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime lastPinged;
    private String result;

    public String getName() {
        return name;
    }

    public PingedURL setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PingedURL setUrl(String url) {
        this.url = url;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PingedURL setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getLastPinged() {
        return lastPinged;
    }

    public PingedURL setLastPinged(LocalDateTime lastPinged) {
        this.lastPinged = lastPinged;
        return this;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
