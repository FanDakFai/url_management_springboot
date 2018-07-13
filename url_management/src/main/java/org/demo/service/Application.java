package org.demo.service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * Spring Boot application that extends from SpringBootServletInitializer
 * so that servlet configuration could be customized
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}


