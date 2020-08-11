package com.vodafone.ebuisness.controller.admin;

import com.vodafone.ebuisness.exception.*;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.MailingService;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import com.vodafone.ebuisness.util.ImageValidator;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/admin/retail")
public class AdminProductManagementController {

    @Autowired
    private ProductsAndCategoriesService productsAndCategoriesService;

    @Autowired
    private MailingService mailingService;

    @PostMapping({"/products/add", "/products/update"})
    public ResponseEntity addProduct(@Valid Product product) {
        Boolean success;
        success = productsAndCategoriesService.saveOrUpdateProduct(product);
        if (success) {
            new Thread(() -> {
                try {
                    mailingService.sendProductNews(product);
                } catch (NoSuchProductException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return success ? new ResponseEntity("success!", HttpStatus.CREATED)
                : new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    @PostMapping({"/productsAndCategories/add", "/productsAndCategories/update"})
    public ResponseEntity addProductWithCategory(@Valid Product product,
                                                 @RequestParam String categoryId)
            throws NoSuchCategoryException {

        Boolean success;
        var category = productsAndCategoriesService.findCategoryById(new ObjectId(categoryId));

        if (product.getCategories() == null) {
            product.setCategories(new ArrayList<>());
        }
        product.getCategories().add(category);

        success = productsAndCategoriesService.saveOrUpdateProduct(product);

        if (success) {
            new Thread(() -> {
                try {
                    mailingService.sendProductNews(product);
                } catch (NoSuchProductException e) {
                    e.printStackTrace();
                }
            }).start();
        }
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

    @PostMapping("/categories/add/to/product")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addCategoryToProduct(
            @RequestParam @NotBlank String categoryId,
            @RequestParam @NotBlank String productId)
            throws NoSuchProductException, NoSuchCategoryException {
        productsAndCategoriesService.addCategoryToProduct(categoryId, productId);
    }

    @DeleteMapping("/categories/remove/from/product")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategoryFromProduct(
            @RequestParam @NotBlank String categoryId,
            @RequestParam @NotBlank String productId)
            throws NoSuchProductException, NoSuchCategoryException {
        productsAndCategoriesService.removeCategoryFromProduct(categoryId, productId);
    }

    @PutMapping("/products/image/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateImagesOfProduct(@RequestParam String productId,
                                      @RequestParam("image") @Max(6) MultipartFile[] images)
            throws NoSuchProductException, IOException, InvalidImageFormatException {

        for (MultipartFile multipartFile : images) {
            if (!(ImageValidator.isImage(multipartFile.getBytes()))) {
                throw new InvalidImageFormatException();
            }
        }

        var product = productsAndCategoriesService.findProductById(new ObjectId(productId));
        product.setImages(new ArrayList<>());
        for (MultipartFile multipartFile : images) {
            product.getImages().add(new Binary(multipartFile.getBytes()));
        }
        productsAndCategoriesService.saveOrUpdateProduct(product);
    }

    @PostMapping("/products/image/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addImageToProduct(@RequestParam String productId,
                                  @RequestParam MultipartFile image)
            throws NoSuchProductException, IOException, NoRoomForImageOfProductException, InvalidImageFormatException {

        if (!(ImageValidator.isImage(image.getBytes()))) {
            throw new InvalidImageFormatException();
        }
        productsAndCategoriesService.addImageToProduct(image.getBytes(), productId);
    }

    @DeleteMapping("/products/image/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearImagesFromProduct(@RequestParam @NotBlank String productId)
            throws NoSuchProductException {
        productsAndCategoriesService.clearImagesFromProduct(productId);
    }

    @DeleteMapping("/products/image/remove/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeImageFromProduct(@RequestParam @NotBlank String productId,
                                       @RequestParam @NotBlank Integer index)
            throws NoSuchProductException, ImageDoesNotExistException {
        productsAndCategoriesService.removeImageFromProduct(index, productId);
    }

    @GetMapping("/products/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getAllImagesOfProduct(@RequestParam @NotBlank String productId,
                                      HttpServletResponse response)
            throws NoSuchProductException, IOException {

        var product = productsAndCategoriesService.findProductById(new ObjectId(productId));
        if (product.getImages() == null) {
            product.setImages(new ArrayList<>());
        }
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (int i = 0; i < product.getImages().size(); i++) {
            ByteArrayInputStream resource = new ByteArrayInputStream(product.getImages().get(i).getData());
            ZipEntry zipEntry = new ZipEntry(Integer.toString(i + 1));
            zipEntry.setSize(resource.available());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(new ByteArrayInputStream(resource.readAllBytes()), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
