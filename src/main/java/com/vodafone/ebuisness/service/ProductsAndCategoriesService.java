package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsAndCategoriesService {

    List<Product> getAllProducts();

    Product findProductById(ObjectId _id);

    Boolean saveOrUpdateProduct(Product product);

    Boolean saveOrUpdateCategory(Category category);

    List<Category> getAllCategories();

    void deleteProduct(ObjectId _id);

    void deleteCategory(ObjectId _id);

}
