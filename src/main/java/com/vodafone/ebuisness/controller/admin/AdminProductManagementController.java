package com.vodafone.ebuisness.controller.admin;

import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.MailingService;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@RestController
@RequestMapping("/admin/retail")
public class AdminProductManagementController {

    @Autowired
    private ProductsAndCategoriesService productsAndCategoriesService;

    @Autowired
    private MailingService mailingService;

    @PostMapping({"/products/add", "/products/update"})
    public ResponseEntity addProduct(@Valid Product product) {
        Boolean success;
        success = productsAndCategoriesService.saveOrUpdateProduct(product);
        if (success) {
            new Thread(() -> {
                try {
                    mailingService.sendProductNews(product);
                } catch (NoSuchProductException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return success ? new ResponseEntity("success!", HttpStatus.CREATED)
                : new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    @PostMapping({"/productsAndCategories/add", "/productsAndCategories/update"})
    public ResponseEntity addProductWithCategory(@Valid Product product,
                                                 @RequestParam String categoryId)
            throws NoSuchCategoryException {

        Boolean success;
        var category = productsAndCategoriesService.findCategoryById(new ObjectId(categoryId));

        if (product.getCategories() == null) {
            product.setCategories(new ArrayList<>());
        }
        product.getCategories().add(category);

        success = productsAndCategoriesService.saveOrUpdateProduct(product);

        if (success) {
            new Thread(() -> {
                try {
                    mailingService.sendProductNews(product);
                } catch (NoSuchProductException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return success ? new ResponseEntity("success!", HttpStatus.CREATED)
                : new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    @PostMapping({"/categories/add", "/categories/update"})
    public ResponseEntity addCategory(@Valid Category category) {
        Boolean success;
        success = productsAndCategoriesService.saveOrUpdateCategory(category);
        return success ? new ResponseEntity("success!", HttpStatus.CREATED)
                : new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/products")
    public ResponseEntity deleteProduct(@RequestParam("id") String id) {

        productsAndCategoriesService.deleteProduct(new ObjectId(id));
        return new ResponseEntity("success!", HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/categories")
    public ResponseEntity deleteCategory(@RequestParam("id") String id) {

        productsAndCategoriesService.deleteCategory(new ObjectId(id));
        return new ResponseEntity("success!", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/categories/add/to/product")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addCategoryToProduct(
            @RequestParam @NotBlank String categoryId,
            @RequestParam @NotBlank String productId)
            throws NoSuchProductException, NoSuchCategoryException {
        productsAndCategoriesService.addCategoryToProduct(categoryId, productId);
    }

    @DeleteMapping("/categories/remove/from/product")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategoryFromProduct(
            @RequestParam @NotBlank String categoryId,
            @RequestParam @NotBlank String productId)
            throws NoSuchProductException, NoSuchCategoryException {
        productsAndCategoriesService.removeCategoryFromProduct(categoryId, productId);
    }

}
