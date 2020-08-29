package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.EmailAlreadyExistException;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.CartService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceImplTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthService authService;

    @BeforeAll
    void setUp() throws EmailAlreadyExistException {

        Account account;

        try {

            account = authService.findAccountByEmail("example@abc.com");

        } catch (EmailDoesNotExistException e) {

            account = null;
        }

        if (account == null) {

            account = new Account();
            Date date = new Date(17, 9, 1995);

            Address address = new Address();
            address.setCountry("Some country");
            address.setProvince("some province");

            PersonName personName
                    = new PersonName("firstName", "middleName", "lastName");

            account.setUsername("some Name");
            account.setPassword("password");
            account.setEmail("example@abc.com");
            account.setPersonName(personName);
            account.setAddress(address);
            account.setDateOfBirth(date);
            authService.register(account);
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProductToCart() {
    }

    @Test
    void updateProductInCart() {
    }

    @Test
    void removeProductFromCart() {
    }

    @Test
    void getAllProductsFromCart() {
    }

    @Test
    void areProductsInCartInStock() {
    }

    @Test
    void isProductInCart() {
    }

    @Test
    void isProductInCartInStock() {
    }

    @Test
    void calculateTotalPriceOfItemsInCart() {
    }

    @Test
    void checkoutCart() {
    }

    @Test
    void cancelInvoice() {
    }
}