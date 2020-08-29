package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.ImageDoesNotExistException;
import com.vodafone.ebuisness.exception.NoRoomForImageOfProductException;
import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.repository.CategoryRepository;
import com.vodafone.ebuisness.repository.ProductRepository;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;

import org.bson.types.Binary;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Product> optionalProduct = null;
        Optional<Category> optionalCategory = null;

        try {
            optionalProduct = productRepository.findById(new ObjectId(productId));
        } catch (IllegalArgumentException e) {
            NoSuchProductException noSuchProductException = new NoSuchProductException("invalid product id");
            noSuchProductException.initCause(e);
            throw noSuchProductException;
        }
        try {
            optionalCategory = categoryRepository.findById(new ObjectId(categoryId));
        } catch (IllegalArgumentException e) {
            NoSuchProductException noSuchProductException = new NoSuchProductException();
            noSuchProductException.initCause(e);
            throw noSuchProductException;

        }

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
    public void deleteProduct(ObjectId _id) throws NoSuchProductException {
if(_id == null){
    throw new NoSuchProductException();
}
        productRepository.deleteById(_id);

    }

    @Override
    public Boolean saveOrUpdateCategory(Category category) {

        category = categoryRepository.save(category);
        return category.getObjectId() != null;

    }

    @Override
    public void deleteCategory(ObjectId _id)
            throws NoSuchCategoryException {
        if (_id == null) {
            throw new NoSuchCategoryException();
        }
        categoryRepository.deleteById(_id);

    }

    @Override
    public void addImageToProduct(byte[] image, String productId)
            throws NoSuchProductException, NoRoomForImageOfProductException {
        Product product = null;
        try {
            product = findProductById(new ObjectId(productId));
        } catch (IllegalArgumentException iae) {
            throw new NoSuchProductException();
        }
        if (product.getImages() == null) {
            product.setImages(new ArrayList<>());
        }
        if (product.getImages().size() == 6) {
            throw new NoRoomForImageOfProductException();
        }
        product.getImages().add(new Binary(image));
        saveOrUpdateProduct(product);

    }

    @Override
    public void clearImagesFromProduct(String productId) throws NoSuchProductException {
        Product product = null;
        try {
            product = findProductById(new ObjectId(productId));
        } catch (IllegalArgumentException e) {
            NoSuchProductException noSuchProductException = new NoSuchProductException();
            noSuchProductException.initCause(e);
            throw noSuchProductException;
        }
        if (product.getImages() == null) {
            return;
        }
        product.getImages().clear();
        saveOrUpdateProduct(product);

    }

    @Override
    public void removeImageFromProduct(Integer index, String productId)
            throws NoSuchProductException, ImageDoesNotExistException {
        if (index == null) {
            throw new ImageDoesNotExistException("No image index defined!!");
        }
        --index;
        Product product = null;
        try {
            product = findProductById(new ObjectId(productId));
        } catch (IllegalArgumentException e) {
            var noSuchProductException = new NoSuchProductException("No product id defined!!");
            noSuchProductException.initCause(e);
            throw noSuchProductException;
        }
        if (product.getImages() == null || product.getImages().size() < index + 1) {
            throw new ImageDoesNotExistException();
        }
        try {
            product.getImages().remove(index.intValue());
        } catch (IndexOutOfBoundsException e) {
            var imageDoesNotExistException = new ImageDoesNotExistException("No image with that index!!");
            imageDoesNotExistException.initCause(e);
            throw imageDoesNotExistException;
        }
        saveOrUpdateProduct(product);

    }

    @Override
    public List<Binary> getImagesOfProduct(String productId) throws NoSuchProductException {
        List<Binary> listOfBinaries = null;
        try {
            listOfBinaries = findProductById(new ObjectId(productId)).getImages();
        } catch (IllegalArgumentException iae) {
            var noSuchProductException = new NoSuchProductException();
            noSuchProductException.initCause(iae);
            throw noSuchProductException;
        }
        return listOfBinaries;
    }

}
