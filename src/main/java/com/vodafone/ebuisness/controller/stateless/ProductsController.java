package com.vodafone.ebuisness.controller.stateless;

import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.websocket.server.PathParam;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        return productsAndCategoriesService.findProductById(new ObjectId(id));
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return productsAndCategoriesService.getAllCategories();
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
