package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.configuration.InvoiceStatus;
import com.vodafone.ebuisness.dto.ProductInStockReport;
import com.vodafone.ebuisness.exception.*;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Invoice;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.repository.ProductsInDealRepository;
import com.vodafone.ebuisness.service.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class CartServiceImplTest {

    public CartServiceImplTest() {

        productsInDealRepository = spy(ProductsInDealRepository.class);
        authService = spy(AuthService.class);
        mailingService = spy(MailingService.class);
        productsAndCategoriesService = spy(ProductsAndCategoriesService.class);
        applicationContext = spy(ApplicationContext.class);
        payPalService = spy(PayPalService.class);

        CartServiceImpl cartServiceImpl = new CartServiceImpl();
        cartServiceImpl.setProductsAndCategoriesService(productsAndCategoriesService);
        cartServiceImpl.setApplicationContext(applicationContext);
        cartServiceImpl.setAuthService(authService);
        cartServiceImpl.setMailingService(mailingService);
        cartServiceImpl.setPayPalService(payPalService);
        cartServiceImpl.setProductsInDealRepository(productsInDealRepository);

        cartService = cartServiceImpl;

    }

    private CartService cartService;

    private ProductsInDealRepository productsInDealRepository;
    private AuthService authService;
    private MailingService mailingService;
    private ProductsAndCategoriesService productsAndCategoriesService;
    private ApplicationContext applicationContext;
    private PayPalService payPalService;

    @Test
    void addProductToCartIfThereAreSomeItemsInTheCartAlready()
            throws EmailDoesNotExistException, NoSuchProductException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        productsInDeal.getProductInCartList().get(0).setRequiredQuantity(1);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail("example@abc.com")).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertDoesNotThrow(() ->
                cartService.addProductToCart("example@abc.com", product.getObjectId().toHexString(), 1));

    }


    @Test
    void addProductToCartIfItemsAreOutOFStock()
            throws EmailDoesNotExistException, NoSuchProductException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        productsInDeal.getProductInCartList().get(0).setRequiredQuantity(20);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail("example@abc.com")).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertThrows(ItemOutOfStockException.class, () ->
                cartService.addProductToCart("example@abc.com", product.getObjectId().toHexString(), 1));

    }

    @Test
    void addProductToCartIfNoItemsInCart()
            throws EmailDoesNotExistException, NoSuchProductException,
            ItemOutOfStockException {

        var productsInDeal = getProductsInDeal();
        var product = getProduct();
        var account = getAccount();

        productsInDeal.setAccount(null);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail("example@abc.com")).thenReturn(account);
        when(applicationContext.getBean(ProductsInDeal.class)).thenReturn(productsInDeal);
        when(applicationContext.getBean(ProductInCart.class)).thenReturn(productsInDeal.getProductInCartList().get(0));

        Assertions.assertDoesNotThrow(() ->
                cartService.addProductToCart("example@abc.com", product.getObjectId().toHexString(), 5));

    }

    @Test
    void updateProductInCartInHappyScenario()
            throws ItemOutOfStockException, NoSuchProductException,
            EmailDoesNotExistException, ItemNotInCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertDoesNotThrow(() ->
                cartService.updateProductInCart("example@abc.com", product.getObjectId().toHexString(), 2));

    }

    @Test
    void updateProductInCartIfProductNotInTheCart()
            throws ItemOutOfStockException, NoSuchProductException,
            EmailDoesNotExistException, ItemNotInCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        productsInDeal = null;

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertThrows(ItemNotInCartException.class, () ->
                cartService.updateProductInCart("example@abc.com", product.getObjectId().toHexString(), 2));

    }

    @Test
    void removeProductFromCartInHappyScenario() throws NoSuchProductException, EmailDoesNotExistException, ItemNotInCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertDoesNotThrow(() ->
                cartService.removeProductFromCart(account.getEmail(), product.getObjectId().toHexString()));

    }

    @Test
    void getAllProductsFromCart() throws EmailDoesNotExistException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        List<ProductInCart> listOfProductsInCarts = cartService.getAllProductsFromCart(account.getEmail());

        Assertions.assertEquals(1, listOfProductsInCarts.size());

    }

    @Test
    void areProductsInCartInStock() throws EmailDoesNotExistException, NoSuchProductException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        List<ProductInStockReport> reportList = cartService.areProductsInCartInStock(account.getEmail());
        Assertions.assertEquals(1, reportList.size());

    }

    @Test
    void isProductInCartIfItIsInCart() throws NoSuchProductException, EmailDoesNotExistException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Boolean isInCart = cartService.isProductInCart(account.getEmail(), product.getObjectId().toHexString());
        Assertions.assertTrue(isInCart);

    }

    @Test
    void isProductInCartIfItIsNotInCart() throws NoSuchProductException, EmailDoesNotExistException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        productsInDeal.getProductInCartList().clear();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Boolean isInCart = cartService.isProductInCart(account.getEmail(), product.getObjectId().toHexString());
        Assertions.assertFalse(isInCart);

    }

    @Test
    void isProductInCartInStockIfItIsInStock() throws NoSuchProductException, EmailDoesNotExistException, ItemNotInCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        ProductInStockReport productInStockReport
                = cartService.isProductInCartInStock(account.getEmail(), product.getObjectId().toHexString());

        Assertions.assertTrue(productInStockReport.getInStock());

    }

    @Test
    void isProductInCartInStockIfItIsNotInStock() throws NoSuchProductException, EmailDoesNotExistException, ItemNotInCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        productsInDeal.getProductInCartList().get(0).setRequiredQuantity(200);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        ProductInStockReport productInStockReport
                = cartService.isProductInCartInStock(account.getEmail(), product.getObjectId().toHexString());

        Assertions.assertFalse(productInStockReport.getInStock());

    }

    @Test
    void isProductInCartInStockIfProductDoesNotExist()
            throws NoSuchProductException, EmailDoesNotExistException,
            ItemNotInCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        productsInDeal.getProductInCartList().get(0).setRequiredQuantity(200);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertThrows(ItemNotInCartException.class, () ->
                cartService.isProductInCartInStock(account.getEmail(), new ObjectId().toHexString()));


    }

    @Test
    void calculateTotalPriceOfItemsInCart()
            throws NoSuchProductException, EmailDoesNotExistException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Double totalPrice = cartService.calculateTotalPriceOfItemsInCart(account.getEmail());

        Assertions.assertEquals(product.getPrice() *
                productsInDeal.getProductInCartList().get(0).getRequiredQuantity(), totalPrice);

    }

    @Test
    void checkoutCartInHappyScenario()
            throws NoSuchProductException, EmailDoesNotExistException,
            MessagingException, ItemOutOfStockException,
            ConnectionErrorException, EmptyCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertDoesNotThrow(() ->
                cartService.checkoutCart(account.getEmail()));

    }

    @Test
    void checkoutCartIfCartIsEmpty()
            throws NoSuchProductException, EmailDoesNotExistException,
            MessagingException, ItemOutOfStockException,
            ConnectionErrorException, EmptyCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);

        Assertions.assertThrows(EmptyCartException.class, () ->
                cartService.checkoutCart(account.getEmail()));

    }

    @Test
    void checkoutCartIfItemsOutOfStock()
            throws NoSuchProductException, EmailDoesNotExistException,
            MessagingException, ItemOutOfStockException,
            ConnectionErrorException, EmptyCartException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();

        productsInDeal.getProductInCartList().get(0).setRequiredQuantity(500);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId())).thenReturn(productsInDeal);

        Assertions.assertThrows(ItemOutOfStockException.class, () ->
                cartService.checkoutCart(account.getEmail()));

    }

    @Test
    void cancelInvoice() throws NoSuchProductException, EmailDoesNotExistException, ConnectionErrorException, NoSuchInvoiceException {

        var productsInDeal = getProductsInDeal();
        var product = productsInDeal.getProductInCartList().get(0).getProduct();
        var account = productsInDeal.getAccount();
        var invoice = new Invoice("1", InvoiceStatus.DRAFT);

        productsInDeal.setInvoice(invoice);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);
        when(authService.findAccountByEmail(account.getEmail())).thenReturn(account);
        when(productsInDealRepository.findProductsInDealByInvoiceId(invoice.getId())).thenReturn(productsInDeal);

        cartService.cancelInvoice(invoice.getId());

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

}