package com.vodafone.ebuisness;

import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.repository.ProductsInDealRepository;
import com.vodafone.ebuisness.service.CartService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EbuisnessApplication implements CommandLineRunner {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductsInDealRepository productsInDealRepository;

    public static void main(String[] args) {
        SpringApplication.run(EbuisnessApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("System started");
    }

}
