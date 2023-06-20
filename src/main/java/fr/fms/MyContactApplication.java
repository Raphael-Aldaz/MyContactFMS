package fr.fms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyContactApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MyContactApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        // TODO document why this method is empty
    }



}
