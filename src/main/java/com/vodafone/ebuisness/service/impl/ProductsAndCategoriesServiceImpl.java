package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.repository.CategoryRepository;
import com.vodafone.ebuisness.repository.ProductRepository;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashSet;
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
    public void addCategoryToProduct(String categoryId, String productId) throws NoSuchProductException, NoSuchCategoryException {

        var optionalProduct = productRepository.findById(new ObjectId(productId));
        var optionalCategory = categoryRepository.findById(new ObjectId(categoryId));
        Product product;
        Category category;

        if (!optionalProduct.isPresent()) {
            throw new NoSuchProductException();
        } else {
            product = optionalProduct.get();
        }
        if (!optionalCategory.isPresent()) {
            throw new NoSuchCategoryException();
        } else {
            category = optionalCategory.get();
        }
        if (product.getCategories() == null) {
            product.setCategories(new ArrayList<>());
        }
        product.getCategories().add(category);
        productRepository.save(product);
    }

    @Override
    public void removeCategoryFromProduct(String categoryId, String productId)
            throws NoSuchProductException, NoSuchCategoryException {
        var optionalProduct = productRepository.findById(new ObjectId(productId));
        var optionalCategory = categoryRepository.findById(new ObjectId(categoryId));
        Product product;
        Category category;

        if (!optionalProduct.isPresent()) {
            throw new NoSuchProductException();
        } else {
            product = optionalProduct.get();
        }
        if (!optionalCategory.isPresent()) {
            throw new NoSuchCategoryException();
        } else {
            category = optionalCategory.get();
        }
        if (!(product.getCategories() == null)) {
            product.getCategories().remove(category);
        }
    }

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

        var optionalProduct = productRepository.findById(_id);
        if (!optionalProduct.isPresent()) {

            throw new NoSuchProductException();

        }
        return optionalProduct.get();
    }

    @Override
    public Category findCategoryById(ObjectId _id)
            throws NoSuchCategoryException {

        var optionalCategory = categoryRepository.findById(_id);
        if (!optionalCategory.isPresent()) {
            throw new NoSuchCategoryException();
        }
        return optionalCategory.get();
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
