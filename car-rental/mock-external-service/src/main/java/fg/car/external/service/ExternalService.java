package fg.car.external.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * Starts application
 */

@SpringBootApplication
public class ExternalService {
    public static void main(String[] args) {
        SpringApplication.run(ExternalService.class, args);
    }
}
