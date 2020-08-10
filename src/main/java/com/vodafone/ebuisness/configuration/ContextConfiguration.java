package com.vodafone.ebuisness.configuration;

import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.util.PropertiesLoader;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.Properties;

@Configuration
public class ContextConfiguration {

    @Autowired
    private PropertiesLoader propertiesLoader;

    @Bean
    @Scope("prototype")
    @Lazy
    public ProductsInDeal getProductsInDeal() {
        var emptyCart = new ProductsInDeal();
        emptyCart.setAccount(new Account());
        emptyCart.setObjectId(new ObjectId());
        emptyCart.setProductInCartList(new ArrayList<ProductInCart>());
        return emptyCart;
    }

    @Bean
    @Scope("prototype")
    @Lazy
    public ProductInCart getProductInCart() {
        var productInCart = new ProductInCart();
        productInCart.setProduct(getProduct());
        productInCart.setRequiredQuantity(0);
        return productInCart;

    }

    @Bean
    @Scope("prototype")
    @Lazy
    public Product getProduct() {
        var product = new Product();
        product.setCategories(new ArrayList<Category>());
        product.setName(new String());
        product.setDescription(new String());
        product.setPrice(0.0);
        product.setObjectId(new ObjectId());
        product.setQuantity(0);
        return product;
    }

    @Bean("Market mail server")
    public JavaMailSender getJavaMailSender() {
        Properties properties
                = propertiesLoader.loadProperties(PropertiesMapping.MARKETING_MAIL);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(properties.getProperty("mail.host"));
        mailSender.setPort(Integer.parseInt(properties.getProperty("mail.port")));
        mailSender.setUsername(properties.getProperty("mail.email"));
        mailSender.setPassword(properties.getProperty("mail.password"));
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

}