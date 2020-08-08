package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.dto.ProductInStockReport;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.ItemNotInCartException;
import com.vodafone.ebuisness.exception.ItemOutOfStockException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;

import java.util.List;

public interface CartService {

    void addProductToCart(String email, String itemId, Integer quantity) throws EmailDoesNotExistException, NoSuchProductException, ItemOutOfStockException;

    void updateProductInCart(String email, String itemId, Integer quantity) throws NoSuchProductException, EmailDoesNotExistException, ItemNotInCartException, ItemOutOfStockException;

    void removeProductFromCart(String email, String itemId) throws NoSuchProductException, EmailDoesNotExistException, ItemNotInCartException;

    List<ProductInCart> getAllProductsFromCart(String email) throws EmailDoesNotExistException;

    List<ProductInStockReport> areProductsInCartInStock(String email) throws EmailDoesNotExistException;

    Boolean isProductInCart(String email, String itemId) throws EmailDoesNotExistException;

    ProductInStockReport isProductInCartInStock(String email, String itemId) throws EmailDoesNotExistException, ItemNotInCartException, NoSuchProductException;

    Double calculateTotalPriceOfItemsInCart(String email) throws EmailDoesNotExistException;

    void checkoutCart(String email);

}
