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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@SpringBootTest
class ProductsAndCategoriesServiceImplTest {

    public ProductsAndCategoriesServiceImplTest() {
        var productsAndCategoriesServiceImpl = new ProductsAndCategoriesServiceImpl();
        categoryRepository = spy(CategoryRepository.class);
        productsAndCategoriesServiceImpl.setCategoryRepository(categoryRepository);
        productRepository = spy(ProductRepository.class);
        productsAndCategoriesServiceImpl.setProductRepository(productRepository);
        productsAndCategoriesService = productsAndCategoriesServiceImpl;
    }

    private ProductsAndCategoriesService productsAndCategoriesService;

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private Product product;
    private Category category;

    private String productDefaultImageLocation
            = "src/test/resources/product image for testing.png";


    @Test
    void saveOrUpdateProduct() {

        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");
        product.setQuantity(50);
        product.setPrice(10000.0);
        product.setDescription("make a relaxing smell");
        when(productRepository.save(product)).thenReturn(product);
        Boolean isProductSaved = productsAndCategoriesService.saveOrUpdateProduct(product);
        Assertions.assertTrue(isProductSaved);

    }

    @Test
    void getAllProducts() {

        Product product = new Product();
        List<Product> listOfProducts = new ArrayList<>();
        listOfProducts.add(product);
        when(productRepository.findAll()).thenReturn(listOfProducts);
        listOfProducts = productsAndCategoriesService.getAllProducts();
        Assertions.assertNotNull(listOfProducts);
        Assertions.assertEquals(1, listOfProducts.size());

    }

    @Test
    void findProductById() throws NoSuchProductException {

        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");
        when(productRepository.findById(product.getObjectId())).thenReturn(Optional.of(product));

        product = productsAndCategoriesService.findProductById(product.getObjectId());

        Assertions.assertNotNull(product);
        Assertions.assertEquals("lavender perfume", product.getName());

    }

    @Test
    void findProductByIdIfProductDoesNotExist() {

        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.findProductById(new ObjectId()));

    }

    @Test
    void saveOrUpdateCategory() {

        Category cosmeticsCategory
                = new Category("Cosmetics", "Make the world beautiful", new HashSet<>());
        cosmeticsCategory.setObjectId(new ObjectId());
        when(categoryRepository.save(cosmeticsCategory)).thenReturn(cosmeticsCategory);
        Boolean isCategorySaved = productsAndCategoriesService.saveOrUpdateCategory(cosmeticsCategory);
        Assertions.assertTrue(isCategorySaved);

    }

    @Test
    void getAllCategories() {

        Category cosmeticsCategory
                = new Category("Cosmetics", "Make the world beautiful", new HashSet<>());
        List<Category> listOfCategories = new ArrayList<>();
        listOfCategories.add(cosmeticsCategory);

        when(categoryRepository.findAll()).thenReturn(listOfCategories);

        listOfCategories = productsAndCategoriesService.getAllCategories();

        Assertions.assertNotNull(listOfCategories);
        Assertions.assertEquals(1, listOfCategories.size());

    }

    @Test
    void findCategoryById() throws NoSuchCategoryException {

        Category cosmeticsCategory
                = new Category("Cosmetics", "Make the world beautiful", new HashSet<>());
        cosmeticsCategory.setObjectId(new ObjectId());
        when(categoryRepository.findById(cosmeticsCategory.getObjectId())).thenReturn(Optional.of(cosmeticsCategory));
        category = productsAndCategoriesService.findCategoryById(cosmeticsCategory.getObjectId());

        Assertions.assertNotNull(category);
        Assertions.assertEquals("Cosmetics", category.getName());

    }

    @Test
    void findCategoryByIdIfCategoryDoesNotExist() {

        Assertions.assertThrows(NoSuchCategoryException.class, () ->
                productsAndCategoriesService.findCategoryById(new ObjectId()));

    }

    @Test
    void addCategoryToProduct() throws NoSuchProductException, NoSuchCategoryException {

        //stubbing category
        Category cosmeticsCategory
                = new Category("Cosmetics", "Make the world beautiful", new HashSet<>());
        cosmeticsCategory.setObjectId(new ObjectId());
        when(categoryRepository.findById(cosmeticsCategory.getObjectId())).thenReturn(Optional.of(cosmeticsCategory));

        //stubbing product
        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");
        when(productRepository.findById(product.getObjectId())).thenReturn(Optional.of(product));

        productsAndCategoriesService.addCategoryToProduct(cosmeticsCategory.getObjectId().toHexString(),
                product.getObjectId().toHexString());

        var productFromDatabase = productsAndCategoriesService.findProductById(product.getObjectId());

        Assertions.assertEquals(1, productFromDatabase.getCategories().size());

    }

    @Test
    void addImageToProduct() {

        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");

        byte[] finalFileContent = loadDefaultProductImage();

        //test happy scenario
        when(productRepository.findById(product.getObjectId())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        Assertions.assertDoesNotThrow(() ->
                productsAndCategoriesService.addImageToProduct(finalFileContent
                        , product.getObjectId().toHexString()));

        //test if no such product
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.addImageToProduct(finalFileContent, new ObjectId().toHexString()));

        //test if no room for more images
        Assertions.assertThrows(NoRoomForImageOfProductException.class, () ->
                {
                    while (true)
                        productsAndCategoriesService.addImageToProduct(finalFileContent, product.getObjectId().toHexString());
                }
        );
    }

    @Test
    void getImagesOfProduct() throws NoSuchProductException {

        byte[] finalFileContent = loadDefaultProductImage();
        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");
        product.setImages(new ArrayList<Binary>());
        product.getImages().add(new Binary(finalFileContent));
        product.getImages().add(new Binary(finalFileContent));
        product.getImages().add(new Binary(finalFileContent));

        when(productRepository.findById(product.getObjectId())).thenReturn(Optional.of(product));

        var listOfBinaries = productsAndCategoriesService.getImagesOfProduct(product.getObjectId().toHexString());

        Assertions.assertNotNull(listOfBinaries);
        Assertions.assertEquals(3, listOfBinaries.size());

        // test if wrong product id used
        Assertions.assertThrows(NoSuchProductException.class, ()
                -> productsAndCategoriesService.getImagesOfProduct(new ObjectId().toHexString()));

    }

    @Test
    void removeImageFromProduct() {

        byte[] finalFileContent = loadDefaultProductImage();
        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");
        product.setImages(new ArrayList<>());
        product.getImages().add(new Binary(finalFileContent));
        product.getImages().add(new Binary(finalFileContent));
        product.getImages().add(new Binary(finalFileContent));

        when(productRepository.findById(product.getObjectId())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Assertions.assertDoesNotThrow(() ->
                productsAndCategoriesService.removeImageFromProduct(3, product.getObjectId().toHexString()));

        //test if no product exist
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.removeImageFromProduct(6, null));

        //when there's no image at specified index then...
        Assertions.assertThrows(ImageDoesNotExistException.class, () ->
                productsAndCategoriesService.removeImageFromProduct(-100, product.getObjectId().toHexString()));

    }

    @Test
    void clearImagesFromProduct() throws NoSuchProductException {

        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");
        product.setImages(new ArrayList<>());

        when(productRepository.findById(product.getObjectId())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Assertions.assertDoesNotThrow(() ->
                productsAndCategoriesService.clearImagesFromProduct(product.getObjectId().toHexString()));
        var productsSize = productsAndCategoriesService.getImagesOfProduct(product.getObjectId().toHexString()).size();
        Assertions.assertEquals(0, productsSize);

        //when wrong product Id used
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.clearImagesFromProduct(new ObjectId().toHexString()));

        //when wrong product Id is null
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.clearImagesFromProduct(null));

    }

    @Test
    void removeCategoryFromProduct() {

        Category cosmeticsCategory
                = new Category("Cosmetics", "Make the world beautiful", new HashSet<>());
        cosmeticsCategory.setObjectId(new ObjectId());

        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");
        product.setImages(new ArrayList<>());
        product.setCategories(new ArrayList<>());
        product.getCategories().add(cosmeticsCategory);

        when(productRepository.findById(product.getObjectId())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(cosmeticsCategory.getObjectId())).thenReturn(Optional.of(cosmeticsCategory));

        Assertions.assertDoesNotThrow(() ->
                productsAndCategoriesService.removeCategoryFromProduct(cosmeticsCategory.getObjectId().toHexString(),
                        product.getObjectId().toHexString()));

        //test if product id is null
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.removeCategoryFromProduct(cosmeticsCategory.getObjectId().toHexString(), null));

        //test if category id is null
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.removeCategoryFromProduct(null, product.getObjectId().toHexString()));

    }

    @Test
    void deleteCategory() {
        Assertions.assertDoesNotThrow(() -> productsAndCategoriesService.deleteCategory(new ObjectId()));
        //when null category id used then .....
        Assertions.assertThrows(NoSuchCategoryException.class, () ->
                productsAndCategoriesService.deleteCategory(null));
    }

    @Test
    void deleteProduct() {
        Assertions.assertDoesNotThrow(() -> productsAndCategoriesService.deleteProduct(new ObjectId()));
        //when null category id used then .....
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.deleteProduct(null));
    }


    private byte[] loadDefaultProductImage() {

        File file = new File(productDefaultImageLocation);
        FileInputStream fin = null;
        byte[] fileContent = null;
        try {
            // create FileInputStream object
            fin = new FileInputStream(file);

            fileContent = new byte[(int) file.length()];

            // Reads up to certain bytes of data from this input stream into an array of bytes.
            fin.read(fileContent);

        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        } finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
        return fileContent;

    }
}