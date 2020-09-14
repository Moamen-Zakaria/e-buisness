package com.vodafone.ebuisness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;


@SpringBootApplication
public class EbuisnessApplication implements CommandLineRunner {

    public EbuisnessApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(EbuisnessApplication.class, args);
    }

    private final Environment env;

    @Override
    public void run(String... args) throws Exception {

        for (int i = 0; i < 5000; i++)
            System.out.println(env.getProperty("spring.data.mongodb.host"));

        System.out.println("System started!");

    }

}
