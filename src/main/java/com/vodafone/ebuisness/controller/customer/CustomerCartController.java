package com.vodafone.ebuisness.controller.customer;

import com.vodafone.ebuisness.dto.ProductInStockReport;
import com.vodafone.ebuisness.exception.*;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.security.util.impl.JwtTokenProviderImpl;
import com.vodafone.ebuisness.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/customer/carts")
public class CustomerCartController {

    @Autowired
    CartService cartService;

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addProductToCart(
            Principal principal
            , @RequestParam @Valid @NotBlank String itemId
            , @RequestParam @Valid @NotBlank @Min(value = 1) Integer quantity)
            throws EmailDoesNotExistException, NoSuchProductException, ItemOutOfStockException {
        cartService.addProductToCart(principal.getName(), itemId, quantity);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updateProductInCart(
            Principal principal
            , @RequestParam @Valid @NotBlank String itemId
            , @RequestParam @Valid @NotBlank @Min(value = 1) Integer quantity)
            throws EmailDoesNotExistException, NoSuchProductException,
            ItemOutOfStockException, ItemNotInCartException {
        cartService.updateProductInCart(principal.getName(), itemId, quantity);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductFromCart(
            Principal principal
            , @RequestParam @Valid @NotBlank String itemId)
            throws EmailDoesNotExistException, NoSuchProductException,
            ItemNotInCartException {
        cartService.removeProductFromCart(principal.getName(), itemId);
    }

    @PostMapping("/checkout")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void checkoutCart(Principal principal)
            throws EmailDoesNotExistException, EmptyCartException,
            MessagingException, ItemOutOfStockException, ConnectionErrorException {
        cartService.checkoutCart(principal.getName());
    }

    @PostMapping("/product/in/cart")
    @ResponseStatus(value = HttpStatus.OK)
    public Boolean isProductInCart(Principal principal,
                                   @RequestParam("item_id") @NotBlank String itemId)
            throws EmailDoesNotExistException {
        return cartService.isProductInCart(principal.getName(), itemId);
    }

    @PostMapping("/product/in/cart/in/stock")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductInStockReport isProductInCartInStock(Principal principal
            , @RequestParam("item_id") @NotBlank String itemId)
            throws EmailDoesNotExistException, ItemNotInCartException, NoSuchProductException {
        return cartService.isProductInCartInStock(principal.getName(), itemId);
    }

    @PostMapping("/products/in/cart/in/stock")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductInStockReport> areProductsInCartInStock(Principal principal)
            throws EmailDoesNotExistException {
        return cartService.areProductsInCartInStock(principal.getName());
    }

    @PostMapping("/products/in/cart")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductInCart> getAllProductsInCart(Principal principal)
            throws EmailDoesNotExistException {
        return cartService.getAllProductsFromCart(principal.getName());
    }

    @PostMapping("/price")
    @ResponseStatus(value = HttpStatus.OK)
    public Double calculateTotalPriceOfItemsInCart(Principal principal)
            throws EmailDoesNotExistException {
        return cartService.calculateTotalPriceOfItemsInCart(principal.getName());
    }

}
