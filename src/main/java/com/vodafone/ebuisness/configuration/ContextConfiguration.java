package com.vodafone.ebuisness.configuration;

import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.CreateInvoiceDraftRequest;
import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.Detail;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.repository.PayPalRepository;
import com.vodafone.ebuisness.repository.impl.PayPalRepositoryImpl;
import com.vodafone.ebuisness.util.PropertiesLoader;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Profile(SpringProfileNames.DEVELOPMENT)
    @Lazy
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

    @Profile(SpringProfileNames.DEVELOPMENT)
    @Lazy
    @Bean("PayPal repository")
    public PayPalRepository getPayPalRepository() {

        Properties properties
                = propertiesLoader.loadProperties(PropertiesMapping.PAYPAL_SANDBOX);
        return new PayPalRepositoryImpl(properties);

    }


    @Bean
    @Lazy
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }


    @Profile(SpringProfileNames.DEVELOPMENT)
    @Bean
    @Scope("prototype")
    @Lazy
    public CreateInvoiceDraftRequest getCreateInvoiceDraftRequest() {

        CreateInvoiceDraftRequest createInvoiceDraftRequest = new CreateInvoiceDraftRequest();
        createInvoiceDraftRequest.setDetail(getDetail());
        createInvoiceDraftRequest.setPrimary_recipients(new ArrayList<>());
        createInvoiceDraftRequest.setItems(new ArrayList<>());
        return createInvoiceDraftRequest;
    }

    @Profile(SpringProfileNames.DEVELOPMENT)
    @Scope("prototype")
    @Bean
    @Lazy
    public Detail getDetail() {

        Detail detail = new Detail();
        detail.setCurrency_code("USD");
        detail.setMemo("Ebuisness Memo!");
        detail.setNote("Ebuisness note!");
        detail.setTerm("Ebuisness term!");
        detail.setReference("Ebuisness reference");
        detail.setInvoice_date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return detail;

    }

}