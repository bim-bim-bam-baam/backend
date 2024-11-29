package org.bimbimbambam.hacktemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.PasswordAuthentication;

@SpringBootApplication
public class HackTemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(HackTemplateApplication.class, args);
    }

}
