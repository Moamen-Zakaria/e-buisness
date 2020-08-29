package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.exception.ImageDoesNotExistException;
import com.vodafone.ebuisness.exception.NoRoomForImageOfProductException;
import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsAndCategoriesService {

    void addCategoryToProduct(String category, String product) throws NoSuchProductException, NoSuchCategoryException;

    void removeCategoryFromProduct(String category, String product) throws NoSuchProductException, NoSuchCategoryException;

    List<Product> getAllProducts();

    Product findProductById(ObjectId _id) throws NoSuchProductException;

    Category findCategoryById(ObjectId _id)
            throws NoSuchCategoryException;

    Boolean saveOrUpdateProduct(Product product);

    Boolean saveOrUpdateCategory(Category category);

    List<Category> getAllCategories();

    void deleteProduct(ObjectId _id) throws NoSuchProductException;

    void deleteCategory(ObjectId _id) throws NoSuchCategoryException;

    void addImageToProduct(byte[] image, String productId) throws NoSuchProductException, NoRoomForImageOfProductException;

    void clearImagesFromProduct(String productId) throws NoSuchProductException;

    void removeImageFromProduct(Integer index, String productId) throws NoSuchProductException, ImageDoesNotExistException;

    List<Binary> getImagesOfProduct(String productId) throws NoSuchProductException;

}
