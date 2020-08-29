package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.*;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductsAndCategoriesServiceImplTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private ProductsAndCategoriesService productsAndCategoriesService;

    private Product product;
    private Category category;

    private String productDefaultImageLocation
            = "src/test/resources/product image for testing.png";


    @BeforeAll
    void setUp() throws EmailAlreadyExistException {

        Account account;

        try {

            account = authService.findAccountByEmail("example@abc.com");

        } catch (EmailDoesNotExistException e) {

            account = null;
        }

        if (account == null) {

            account = new Account();
            Date date = new Date(17, 9, 1995);

            Address address = new Address();
            address.setCountry("Some country");
            address.setProvince("some province");

            PersonName personName
                    = new PersonName("firstName", "middleName", "lastName");

            account.setUsername("some Name");
            account.setPassword("password");
            account.setEmail("example@abc.com");
            account.setPersonName(personName);
            account.setAddress(address);
            account.setDateOfBirth(date);
            authService.register(account);
        }

    }

    @Test
    @Order(1)
    void saveOrUpdateProduct() {

        Product product = new Product();
        product.setName("lavender perfume");
        product.setQuantity(50);
        product.setPrice(10000.0);
        product.setDescription("make a relaxing smell");
        Boolean isProductSaved = productsAndCategoriesService.saveOrUpdateProduct(product);
        this.product = product;

        Assertions.assertTrue(isProductSaved);

    }

    @Test
    @Order(2)
    void getAllProducts() {

        var listOfProducts = productsAndCategoriesService.getAllProducts();

        Assertions.assertNotNull(listOfProducts);
        Assertions.assertEquals(1, listOfProducts.size());

    }

    @Test
    @Order(3)
    void findProductById() throws NoSuchProductException {
        var listOfProducts = productsAndCategoriesService.getAllProducts();

        var productId = listOfProducts.stream().filter(product -> product.getName()
                .equals("lavender perfume")).collect(Collectors.toList()).get(0).getObjectId();
        this.product.setObjectId(productId);

        Product product = productsAndCategoriesService.findProductById(productId);

        Assertions.assertNotNull(product);
        Assertions.assertEquals("lavender perfume", product.getName());

    }

    @Test
    @Order(4)
    void findProductByIdIfProductDoesNotExist() {

        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.findProductById(new ObjectId()));

    }

    @Test
    @Order(5)
    void saveOrUpdateCategory() {

        Category cosmeticsCategory
                = new Category("Cosmetics", "Make the world beautiful", new HashSet<>());
        Boolean isCategorySaved = productsAndCategoriesService.saveOrUpdateCategory(cosmeticsCategory);
        this.category = cosmeticsCategory;

        Assertions.assertTrue(isCategorySaved);

    }

    @Test
    @Order(6)
    void getAllCategories() {

        var listOfCategories = productsAndCategoriesService.getAllCategories();

        Assertions.assertNotNull(listOfCategories);
        Assertions.assertEquals(1, listOfCategories.size());

    }

    @Test
    @Order(7)
    void findCategoryById() throws NoSuchCategoryException {

        var listOfCategories = productsAndCategoriesService.getAllCategories();

        var categoryId = listOfCategories.stream().filter(product -> product.getName()
                .equals("Cosmetics")).collect(Collectors.toList()).get(0).getObjectId();
        this.category.setObjectId(categoryId);

        Category category = productsAndCategoriesService.findCategoryById(categoryId);

        Assertions.assertNotNull(category);
        Assertions.assertEquals("Cosmetics", category.getName());

    }

    @Test
    @Order(8)
    void findCategoryByIdIfCategoryDoesNotExist() {

        Assertions.assertThrows(NoSuchCategoryException.class, () ->
                productsAndCategoriesService.findCategoryById(new ObjectId()));

    }

    @Test
    @Order(9)
    void addCategoryToProduct() throws NoSuchProductException, NoSuchCategoryException {

        productsAndCategoriesService.addCategoryToProduct(category.getObjectId().toHexString(),
                product.getObjectId().toHexString());

        var productFromDatabase = productsAndCategoriesService.findProductById(product.getObjectId());

        Assertions.assertEquals(1, productFromDatabase.getCategories().size());

    }

    @Test
    @Order(10)
    void addImageToProduct() {


        byte[] finalFileContent = loadDefaultProductImage();
        //test happy scenario
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
    @Order(11)
    void getImagesOfProduct() throws NoSuchProductException {

        var listOfBinaries = productsAndCategoriesService.getImagesOfProduct(product.getObjectId().toHexString());

        Assertions.assertNotNull(listOfBinaries);
        Assertions.assertEquals(6, listOfBinaries.size());

        // test if wrong product id used
        Assertions.assertThrows(NoSuchProductException.class, ()
                -> productsAndCategoriesService.getImagesOfProduct(new ObjectId().toHexString()));

    }

    @Test
    @Order(12)
    void removeImageFromProduct() {

        Assertions.assertDoesNotThrow(() ->
                productsAndCategoriesService.removeImageFromProduct(6, product.getObjectId().toHexString()));

        //test if no image exist
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.removeImageFromProduct(6, null));

        //when there's no image at specified index then...
        Assertions.assertThrows(ImageDoesNotExistException.class, () ->
                productsAndCategoriesService.removeImageFromProduct(-100, product.getObjectId().toHexString()));

    }

    @Test
    @Order(13)
    void clearImagesFromProduct() throws NoSuchProductException {
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
    void removeCategoryFromProduct() throws NoSuchProductException, NoSuchCategoryException {
        Assertions.assertDoesNotThrow(() ->
                productsAndCategoriesService.removeCategoryFromProduct(category.getObjectId().toHexString(),
                        product.getObjectId().toHexString()));

        //test if product id is null
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.removeCategoryFromProduct(category.getObjectId().toHexString(), null));

        //test if category id is null
        Assertions.assertThrows(NoSuchProductException.class, () ->
                productsAndCategoriesService.removeCategoryFromProduct(null, product.getObjectId().toHexString()));


    }

    @Test
    void deleteCategory() throws NoSuchCategoryException {
        Assertions.assertDoesNotThrow(() -> productsAndCategoriesService.deleteCategory(category.getObjectId()));
        //when null category id used then .....
        Assertions.assertThrows(NoSuchCategoryException.class, () ->
                productsAndCategoriesService.deleteCategory(null));
    }

    @Test
    void deleteProduct() {
        Assertions.assertDoesNotThrow(() -> productsAndCategoriesService.deleteProduct(product.getObjectId()));
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