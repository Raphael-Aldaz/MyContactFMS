package fr.fms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;

@SpringBootApplication
public class MyContactApplication implements CommandLineRunner , ErrorController {

    public static void main(String[] args) {
        SpringApplication.run(MyContactApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
    }



}
