package se.nackademin.devops24.pingurl.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public URLRepository urlRepository() {
        return new MemoryURLRepository();
    }
}
