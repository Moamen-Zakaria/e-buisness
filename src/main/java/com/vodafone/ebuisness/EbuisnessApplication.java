package com.vodafone.ebuisness;

import com.vodafone.ebuisness.configuration.AccountRole;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.repository.PayPalRepository;
import com.vodafone.ebuisness.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@SpringBootApplication
public class EbuisnessApplication implements CommandLineRunner {

    @Autowired
    AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(EbuisnessApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        Account account = new Account();
//        account.setEmail("moamen.zakaria@gmail.com");
//        account.setPersonName(new PersonName("Moamen" , "Mortada", "Mohamed"));
//        account.setPassword("password");
//        account.setUsername("navras");
//        Address address = new Address();
//        address.setCity("Zion");
//        address.setCountry("UK");
//        address.setDistrict("Mars");
//
//        account.setAddress(address);
//        account.setRoles(new HashSet<>(){{add(AccountRole.ADMIN);}});
//        authService.register(account);
        System.out.println("System started");
    }

}
