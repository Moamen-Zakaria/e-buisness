package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.repository.CategoryRepository;
import com.vodafone.ebuisness.repository.ProductRepository;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;

import org.bson.types.ObjectId;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductsAndCategoriesServiceImpl implements ProductsAndCategoriesService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Product findProductById(ObjectId _id) throws NoSuchProductException {

        if (!productRepository.findById(_id).isPresent()) {

            throw new NoSuchProductException();

        }
        return productRepository.findById(_id).get();
    }

    @Override
    public Boolean saveOrUpdateProduct(Product product) {

        product = productRepository.save(product);
        return product.getObjectId() != null;

    }

    @Override
    public void deleteProduct(ObjectId _id) {

        productRepository.deleteById(_id);

    }

    @Override
    public Boolean saveOrUpdateCategory(Category category) {

        category = categoryRepository.save(category);
        return category.getObjectId() != null;

    }

    @Override
    public void deleteCategory(ObjectId _id) {

        categoryRepository.deleteById(_id);

    }

}
