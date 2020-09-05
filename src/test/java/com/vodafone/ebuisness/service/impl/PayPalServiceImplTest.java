package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.dto.paypal.Token;
import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.CreateInvoiceDraftRequest;
import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.Detail;
import com.vodafone.ebuisness.exception.ConnectionErrorException;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.CartService;
import com.vodafone.ebuisness.util.Adapters;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PayPalServiceImplTest {

    private PayPalServiceImpl payPalService;

    private RestTemplate restTemplate;
    private AuthService authService;
    private CartService cartService;
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {

        Properties properties = new Properties();

        properties.put("paypal.api.url", "url");
        properties.put("paypal.client.id", "id");
        properties.put("paypal.client.secret", "secret");

        payPalService = new PayPalServiceImpl(properties);

        restTemplate = mock(RestTemplate.class);
        authService = spy(AuthService.class);
        cartService = spy(CartService.class);
        applicationContext = spy(ApplicationContext.class);

        payPalService.setApplicationContext(applicationContext);
        payPalService.setCartService(cartService);
        payPalService.setRestTemplate(restTemplate);
        payPalService.setAuthService(authService);

    }

    @Test
    void sendInvoice() throws ConnectionErrorException {

        ResponseEntity<String> stringResponse = new ResponseEntity<>("{\"href\":\"link\"}", HttpStatus.ACCEPTED);
        ResponseEntity<Token> tokenResponse = new ResponseEntity<>(getToken(), HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(stringResponse);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(Token.class)))
                .thenReturn(tokenResponse);
        payPalService.sendInvoice("1");
        verify(restTemplate).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));
        verify(restTemplate).postForEntity(any(String.class), any(HttpEntity.class), eq(Token.class));

    }

    @Test
    void createDraftInvoice() throws ConnectionErrorException {

        //preparing test data
        ProductsInDeal productsInDeal = getProductsInDeal();
        Account account = getAccount();
        Product product = getProduct();
        productsInDeal.setProductInCartList(new ArrayList<>(Arrays.asList(new ProductInCart(product, 10))));
        CreateInvoiceDraftRequest createInvoiceDraftRequest = getCreateInvoiceDraftRequest();
        createInvoiceDraftRequest.setDetail(getDetail());
        createInvoiceDraftRequest
                .setPrimary_recipients(new ArrayList<>(Arrays.asList(Adapters.convertAccountToPrimaryRecipient(account))));
        createInvoiceDraftRequest
                .setItems(new ArrayList<>(Arrays.asList(Adapters.convertProductToItem(productsInDeal.getProductInCartList().get(0)))));

        ResponseEntity<String> stringResponse = new ResponseEntity<>("{\"href\":\"link\"}", HttpStatus.CREATED);
        ResponseEntity<Token> tokenResponse = new ResponseEntity<>(getToken(), HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(Token.class)))
                .thenReturn(tokenResponse);
        when(applicationContext.getBean(CreateInvoiceDraftRequest.class)).thenReturn(createInvoiceDraftRequest);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class))).thenReturn(stringResponse);
        Assertions.assertDoesNotThrow(() ->
                payPalService.createDraftInvoice(productsInDeal, account));
        verify(restTemplate).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));


    }

    @Test
    void generateInvoiceNumber() throws ConnectionErrorException {

        ResponseEntity<String> stringResponse = new ResponseEntity<>("{\"href\":\"link\"}", HttpStatus.OK);
        ResponseEntity<Token> tokenResponse = new ResponseEntity<>(getToken(), HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(Token.class)))
                .thenReturn(tokenResponse);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class))).thenReturn(stringResponse);

        Assertions.assertDoesNotThrow(() ->
                payPalService.generateInvoiceNumber());
        verify(restTemplate).postForEntity(any(String.class), any(HttpEntity.class), eq(Token.class));
        verify(restTemplate).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));


    }

    @Test
    void cancelInvoice() throws ConnectionErrorException {

        ResponseEntity<String> stringResponse = new ResponseEntity<>("{\"href\":\"link\"}", HttpStatus.OK);
        ResponseEntity<Token> tokenResponse = new ResponseEntity<>(getToken(), HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(Token.class)))
                .thenReturn(tokenResponse);

        Assertions.assertDoesNotThrow(() ->
                payPalService.cancelInvoice("id"));

        verify(restTemplate).postForEntity(any(String.class), any(HttpEntity.class), eq(Token.class));

    }

    private Detail getDetail() {

        Detail detail = new Detail();
        detail.setCurrency_code("USD");
        detail.setMemo("Memo");
        detail.setNote("Note");
        detail.setTerm("Term");
        detail.setReference("Reference");
        detail.setInvoice_date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return detail;

    }

    private Token getToken() {

        Token token = new Token();
        token.setAccess_token("SomeRandomToken");
        token.setExpires_in(1000);
        token.setNonce("Nonce");
        token.setApp_id("AppId");
        token.setScope("token");
        token.setToken_type("refreshToken");

        return token;
    }

    private Account getAccount() {

        Date date = new Date(17, 9, 1995);

        Address address = new Address();
        address.setCountry("Some country");
        address.setProvince("some province");

        PersonName personName
                = new PersonName("firstName", "middleName", "lastName");

        Account account = new Account();
        account.setObjectId(new ObjectId());
        account.setUsername("some Name");
        account.setPassword("password");
        account.setEmail("example@abc.com");
        account.setPersonName(personName);
        account.setAddress(address);
        account.setDateOfBirth(date);

        return account;
    }

    private Product getProduct() {

        Product product = new Product();
        product.setQuantity(5);
        product.setPrice(1000.0);
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");

        return product;
    }

    private ProductsInDeal getProductsInDeal() {

        ProductInCart productInCart = new ProductInCart(getProduct(), 5);

        List<ProductInCart> productInCartList = new ArrayList<>();
        productInCartList.add(productInCart);

        ProductsInDeal productsInDeal = new ProductsInDeal();
        productsInDeal.setObjectId(new ObjectId());
        productsInDeal.setAccount(getAccount());
        productsInDeal.setProductInCartList(productInCartList);

        return productsInDeal;

    }

    private CreateInvoiceDraftRequest getCreateInvoiceDraftRequest() {

        CreateInvoiceDraftRequest createInvoiceDraftRequest = new CreateInvoiceDraftRequest();
        createInvoiceDraftRequest.setDetail(getDetail());

        return createInvoiceDraftRequest;

    }

}