package com.vodafone.ebuisness.controller.stateless;

import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsAndCategoriesService productsAndCategoriesService;

    @GetMapping("/")
    public List<Product> getProducts() {
        return productsAndCategoriesService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") String id) throws NoSuchProductException {
        System.out.println(id);
        return productsAndCategoriesService.findProductById(new ObjectId(id));
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return productsAndCategoriesService.getAllCategories();
    }

}
