package com.vodafone.ebuisness.controller.customer;

import com.vodafone.ebuisness.exception.*;
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

@RestController
@RequestMapping("/customer/carts")
public class CustomerCartController {

    @Autowired
    CartService cartService;

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addProductToCart(
            @RequestParam @Valid @NotBlank String email
            , @RequestParam @Valid @NotBlank String itemId
            , @RequestParam @Valid @NotBlank @Min(value = 1) Integer quantity)
            throws EmailDoesNotExistException, NoSuchProductException, ItemOutOfStockException {
        cartService.addProductToCart(email, itemId, quantity);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updateProductInCart(
            @RequestParam @Valid @NotBlank String email
            , @RequestParam @Valid @NotBlank String itemId
            , @RequestParam @Valid @NotBlank @Min(value = 1) Integer quantity)
            throws EmailDoesNotExistException, NoSuchProductException,
            ItemOutOfStockException, ItemNotInCartException {
        cartService.updateProductInCart(email, itemId, quantity);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductFromCart(
            @RequestParam @Valid @NotBlank String email
            , @RequestParam @Valid @NotBlank String itemId)
            throws EmailDoesNotExistException, NoSuchProductException,
            ItemNotInCartException {
        cartService.removeProductFromCart(email, itemId);
    }

    @PostMapping("/checkout")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void checkoutCart(
            @RequestParam @Valid @NotBlank String email)
            throws EmailDoesNotExistException, EmptyCartException,
            MessagingException, ItemOutOfStockException {
        cartService.checkoutCart(email);
    }

}
