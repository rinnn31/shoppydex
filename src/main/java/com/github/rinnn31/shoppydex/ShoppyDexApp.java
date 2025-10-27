package com.github.rinnn31.shoppydex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShoppyDexApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppyDexApp.class, args);
    }
}