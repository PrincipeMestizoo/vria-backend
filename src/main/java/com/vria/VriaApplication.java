package com.vria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VriaApplication.class, args);
    }

}
