package com.vodafone.ebuisness;

import com.vodafone.ebuisness.repository.PayPalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;


@SpringBootApplication
public class EbuisnessApplication implements CommandLineRunner {

    @Autowired
    PayPalRepository payPalRepository;

    public static void main(String[] args) {
        SpringApplication.run(EbuisnessApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("System started");
    }

}
