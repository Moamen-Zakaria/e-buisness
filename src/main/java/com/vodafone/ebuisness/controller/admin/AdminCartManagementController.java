package com.vodafone.ebuisness.controller.admin;

import com.vodafone.ebuisness.dto.ProductInStockReport;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.ItemNotInCartException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/admin/carts")
public class AdminCartManagementController {

    @Autowired
    CartService cartService;

    @PostMapping("/product/in/cart")
    @ResponseStatus(value = HttpStatus.OK)
    public Boolean isProductInCart(@RequestParam @NotBlank String email
            , @RequestParam("item_id") @NotBlank String itemId) throws EmailDoesNotExistException {
        return cartService.isProductInCart(email, itemId);
    }

    @PostMapping("/product/in/cart/in/stock")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductInStockReport isProductInCartInStock(@RequestParam @NotBlank String email
            , @RequestParam("item_id") @NotBlank String itemId)
            throws EmailDoesNotExistException, ItemNotInCartException,
            NoSuchProductException {
        return cartService.isProductInCartInStock(email, itemId);
    }

    @PostMapping("/products/in/cart/in/stock")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductInStockReport> areProductsInCartInStock(@RequestParam @Email @NotBlank String email)
            throws EmailDoesNotExistException {
        return cartService.areProductsInCartInStock(email);
    }

    @PostMapping("/products/in/cart")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductInCart> getAllProductsInCart(@RequestParam @NotBlank @Email String email)
            throws EmailDoesNotExistException {
        return cartService.getAllProductsFromCart(email);
    }

    @PostMapping("/price")
    @ResponseStatus(value = HttpStatus.OK)
    public Double calculateTotalPriceOfItemsInCart(
            @RequestParam @NotBlank @Email String email)
            throws EmailDoesNotExistException {
        return cartService.calculateTotalPriceOfItemsInCart(email);
    }

}
