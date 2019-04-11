package com.paradise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * starter
 *
 * @author Paradise
 */
@EnableScheduling
@SpringBootApplication
public class TransitMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransitMonitorApplication.class, args);
    }

}
