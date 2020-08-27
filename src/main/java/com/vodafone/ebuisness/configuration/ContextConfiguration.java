package com.vodafone.ebuisness.configuration;

import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.CreateInvoiceDraftRequest;
import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.Detail;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.service.PayPalService;
import com.vodafone.ebuisness.service.impl.PayPalServiceImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
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
@PropertySource("classpath:Paypal details/PayPal-${spring.profiles.active}.properties")
@PropertySource("classpath:Mail profiles/Marketing-${spring.profiles.active}.properties")
@PropertySource("classpath:Database/Database-${spring.profiles.active}.properties")
public class ContextConfiguration {

    //email properties
    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private Integer port;

    @Value("${mail.email}")
    private String email;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.transport.protocol}")
    private String protocol;

    @Value("${mail.smtp.ssl.enable}")
    private Boolean isSSLEnabled;

    @Value("${mail.properties.mail.smtp.auth}")
    private Boolean isAuthEnabled;

    @Value("${mail.properties.mail.smtp.starttls.enable}")
    private Boolean isStarttlsEnabled;

    @Value("${mail.debug}")
    private Boolean isDebugEnabled;

    //PayPal properties
    @Value("${paypal.client.id}")
    private String payPalId;

    @Value("${paypal.client.secret}")
    private String payPalSecret;

    @Value("${paypal.api.url}")
    private String payPalUrl;

    @Value("${invoice.memo}")
    private String payPalInvoiceMemo;

    @Value("${invoice.note}")
    private String payPalInvoiceNote;

    @Value("${invoice.term}")
    private String payPalInvoiceTerm;

    @Value("${invoice.reference}")
    private String payPalInvoiceReference;

    @Value("${invoice.currency}")
    private String payPalInvoiceCurrency;

//    @Autowired
//    private PropertiesLoader propertiesLoader;

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

    @Lazy
    @Bean("Market mail server")
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties properties = new Properties();

        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", isAuthEnabled);
        properties.put("mail.smtp.starttls.enable", isStarttlsEnabled);
        properties.put("mail.debug", isDebugEnabled);
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

    @Lazy
    @Bean("PayPal repository")
    public PayPalService getPayPalRepository() {

        Properties properties = new Properties();
        properties.put("paypal.client.id", payPalId);
        properties.put("paypal.client.secret", payPalSecret);
        properties.put("paypal.api.url", payPalUrl);

        return new PayPalServiceImpl(properties);

    }


    @Bean
    @Lazy
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }


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

    @Scope("prototype")
    @Bean
    @Lazy
    public Detail getDetail() {

        Detail detail = new Detail();
        detail.setCurrency_code(payPalInvoiceCurrency);
        detail.setMemo(payPalInvoiceMemo);
        detail.setNote(payPalInvoiceNote);
        detail.setTerm(payPalInvoiceTerm);
        detail.setReference(payPalInvoiceReference);
        detail.setInvoice_date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return detail;

    }

}