package com.vodafone.ebuisness.controller.admin;

import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/retail")
public class AdminProductManagementController {

    @Autowired
    private ProductsAndCategoriesService productsAndCategoriesService;


    @PostMapping({"/products/add", "/products/update"})
    public ResponseEntity addProduct(@Valid Product product) {
        Boolean success;
        success = productsAndCategoriesService.saveOrUpdateProduct(product);
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

}
