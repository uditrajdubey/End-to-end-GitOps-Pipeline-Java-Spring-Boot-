package com.minikube.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration
@RestController  // Add this annotation
public class MinikubeSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinikubeSampleApplication.class, args);
    }

    // GET endpoint for "/"
    @GetMapping("/")
    public String hello() {
        return "Devops is Good!";
    }
}
