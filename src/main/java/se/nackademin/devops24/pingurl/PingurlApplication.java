package se.nackademin.devops24.pingurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PingurlApplication {
    static void main(String[] args) {
        SpringApplication.run(PingurlApplication.class, args);
    }
}
