package org.example.rgybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RgyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RgyBackendApplication.class, args);
    }

}
