package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.configuration.InvoiceStatus;
import com.vodafone.ebuisness.dto.ProductInStockReport;
import com.vodafone.ebuisness.exception.*;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Invoice;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.service.PayPalService;
import com.vodafone.ebuisness.repository.ProductsInDealRepository;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.CartService;
import com.vodafone.ebuisness.service.MailingService;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductsInDealRepository productsInDealRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private ProductsAndCategoriesService productsAndCategoriesService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PayPalService payPalService;

    @Override
    public void addProductToCart(String email, String itemId, Integer quantity)
            throws EmailDoesNotExistException, NoSuchProductException, ItemOutOfStockException {

        var product = productsAndCategoriesService.findProductById(new ObjectId(itemId));
        var account = authService.findAccountByEmail(email);
        var productInDeal
                = productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId());
        if (productInDeal == null) {
            productInDeal = applicationContext.getBean(ProductsInDeal.class);
            productInDeal.setAccount(account);
        }
        var productInCartList = productInDeal.getProductInCartList();
        var optionalProductInCart = productInCartList.stream().parallel()
                .filter(pic -> pic.getProduct().getObjectId().equals(product.getObjectId())).findFirst();
        ProductInCart productInCart;

        if (optionalProductInCart.isPresent()) {
            productInCart = optionalProductInCart.get();
            productInCart.setRequiredQuantity(productInCart.getRequiredQuantity() + quantity);
        } else {
            productInCart = applicationContext.getBean(ProductInCart.class);
            productInCart.setRequiredQuantity(quantity);
            productInCart.setProduct(product);
            productInCartList.add(productInCart);
        }

        if (productInCart.isInStock()) {
            productsInDealRepository.save(productInDeal);
        } else {
            throw new ItemOutOfStockException();
        }
    }

    @Override
    public void updateProductInCart(String email, String itemId, Integer quantity)
            throws NoSuchProductException, EmailDoesNotExistException,
            ItemNotInCartException, ItemOutOfStockException {

        var product = productsAndCategoriesService.findProductById(new ObjectId(itemId));
        var account = authService.findAccountByEmail(email);
        var productInDeal
                = productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId());
        if (productInDeal == null) {
            throw new ItemNotInCartException();
        }
        var productInCartList = productInDeal.getProductInCartList();
        var optionalProductInCart = productInCartList.stream().parallel()
                .filter(pic -> pic.getProduct().getObjectId().equals(product.getObjectId())).findFirst();
        ProductInCart productInCart;

        if (optionalProductInCart.isPresent()) {
            productInCart = optionalProductInCart.get();
            productInCart.setRequiredQuantity(quantity);
        } else {
            throw new ItemNotInCartException();
        }

        if (productInCart.isInStock()) {
            productsInDealRepository.save(productInDeal);
        } else {
            throw new ItemOutOfStockException();
        }
    }

    @Override
    public void removeProductFromCart(String email, String itemId)
            throws NoSuchProductException, EmailDoesNotExistException, ItemNotInCartException {

        var product = productsAndCategoriesService.findProductById(new ObjectId(itemId));
        var account = authService.findAccountByEmail(email);
        var productInDeal
                = productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId());
        if (productInDeal == null) {
            throw new ItemNotInCartException();
        }
        var productInCartList = productInDeal.getProductInCartList();
        var optionalProductInCart = productInCartList.stream().parallel()
                .filter(pic -> pic.getProduct().getObjectId().equals(product.getObjectId())).findFirst();
        ProductInCart productInCart;

        if (optionalProductInCart.isPresent()) {
            productInCart = optionalProductInCart.get();
            productInCartList.remove(productInCart);
        } else {
            throw new ItemNotInCartException();
        }
        if (productInCartList.size() == 0) {
            productsInDealRepository.delete(productInDeal);
        } else {
            productsInDealRepository.save(productInDeal);
        }

    }

    @Override
    public List<ProductInCart> getAllProductsFromCart(String email)
            throws EmailDoesNotExistException {

        var account = authService.findAccountByEmail(email);
        var productsInDeal
                = productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId());
        if (productsInDeal == null || productsInDeal.getProductInCartList() == null)
            return new ArrayList<>();
        return productsInDeal.getProductInCartList();
    }

    @Override
    public List<ProductInStockReport> areProductsInCartInStock(String email)
            throws EmailDoesNotExistException {

        var productInStockReportList = new ArrayList<ProductInStockReport>();
        for (ProductInCart productInCart : getAllProductsFromCart(email)) {
            var product = productInCart.getProduct();
            var productInStockReport
                    = new ProductInStockReport(product.getName()
                    , product.getQuantity()
                    , productInCart.getRequiredQuantity());
            productInStockReport.setObjectId(productInCart.getProduct().getObjectId());

            productInStockReportList.add(productInStockReport);
        }
        return productInStockReportList;
    }

    @Override
    public Boolean isProductInCart(String email, String itemId)
            throws EmailDoesNotExistException {

        var account = authService.findAccountByEmail(email);
        var accountId = account.getObjectId();
        var productInDeal = productsInDealRepository.findByAccount_IdAndPaymentIsNull(accountId);
        if (productInDeal == null) {
            return false;
        }
        if (productInDeal.getProductInCartList() != null) {
            var optionalId = productInDeal.getProductInCartList().stream().parallel()
                    .filter(pid -> pid.getProduct().getObjectId().equals(new ObjectId(itemId))).findFirst();
            return optionalId.isPresent();
        }
        return false;
    }

    @Override
    public ProductInStockReport isProductInCartInStock(String email, String itemId)
            throws EmailDoesNotExistException, ItemNotInCartException, NoSuchProductException {

        if (!isProductInCart(email, itemId)) {
            throw new ItemNotInCartException();
        }
        var account = authService.findAccountByEmail(email);
        var product = productsAndCategoriesService.findProductById(new ObjectId(itemId));
        var productInDeal = productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId());
        var productInCartList = productInDeal.getProductInCartList();
        var optionalProductInCart = productInCartList.stream().parallel().filter(pic -> pic.getProduct().getObjectId().equals(product.getObjectId()))
                .findFirst();
        var productInCart = optionalProductInCart.get();
        var productInStockReport = new ProductInStockReport(product.getName(), product.getQuantity(), productInCart.getRequiredQuantity());
        productInStockReport.setObjectId(product.getObjectId());
        return productInStockReport;
    }

    @Override
    public Double calculateTotalPriceOfItemsInCart(String email)
            throws EmailDoesNotExistException {

        var productInCartList = getAllProductsFromCart(email);
        Double totalPrice = 0.0;
        for (ProductInCart productInCart : productInCartList) {
            totalPrice += (productInCart.getProduct().getPrice() * productInCart.getRequiredQuantity());
        }
        return totalPrice;
    }

    @Override
    @Transactional
    public void checkoutCart(String email)
            throws EmailDoesNotExistException, EmptyCartException,
            ItemOutOfStockException, ConnectionErrorException {

        //validating that the email is registered
        Account account = authService.findAccountByEmail(email);
        ProductsInDeal productsInDeal
                = productsInDealRepository.findByAccount_IdAndPaymentIsNull(account.getObjectId());

        if (productsInDeal == null) {
            throw new EmptyCartException();
        }
        var listOfReports = areProductsInCartInStock(email);

        //validating that items are in stock
        for (ProductInStockReport report : listOfReports) {
            if (!report.getInStock()) {
                throw new ItemOutOfStockException();
            }
        }

        //updating database products' quantities
        listOfReports.stream().parallel().forEach(report -> {

            try {
                var product = productsAndCategoriesService.findProductById(report.getObjectId());
                product.setQuantity(product.getQuantity() - report.getRequiredQuantity());
                productsAndCategoriesService.saveOrUpdateProduct(product);
            } catch (NoSuchProductException e) {
                e.printStackTrace();
            }

        });

        String invoiceId = payPalService.createDraftInvoice(productsInDeal, account);
        var link = payPalService.sendInvoice(invoiceId);
        new Thread(() -> {
            try {
                mailingService.sendInvoiceMail(account, link);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
        productsInDeal.setInvoice(new Invoice(invoiceId, InvoiceStatus.SENT));
        productsInDealRepository.save(productsInDeal);

    }

    @Override
    @Transactional
    public void cancelInvoice(String invoiceId) throws ConnectionErrorException, NoSuchInvoiceException {
        var productsInDeal =
                productsInDealRepository.findProductsInDealByInvoiceId(invoiceId);
        if(productsInDeal == null){
            throw new NoSuchInvoiceException();
        }
        payPalService.cancelInvoice(invoiceId);
        productsInDeal.getInvoice().setStatus(InvoiceStatus.CANCELLED);
        productsInDealRepository.save(productsInDeal);
    }

}
