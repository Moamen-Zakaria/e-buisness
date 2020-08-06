package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsAndCategoriesService {

    public List<Product> getAllProducts();
    public Product findProductById(ObjectId _id);
    public Boolean saveOrUpdateProduct(Product product);
    public Boolean saveOrUpdateCategory(Category category);
    public List<Category> getAllCategories();
    public void deleteProduct(ObjectId _id);
    public void deleteCategory(ObjectId _id);

}
