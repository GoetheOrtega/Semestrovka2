package com.example.semestrovka2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class Semestrovka2Application {

     public static void main(String[] args) {
        SpringApplication.run(Semestrovka2Application.class, args);
    }

}
