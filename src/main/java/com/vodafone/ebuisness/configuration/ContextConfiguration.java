package com.vodafone.ebuisness.configuration;

import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

@Configuration
public class ContextConfiguration {

    @Bean
    @Scope("prototype")
    @Lazy
    public ProductsInDeal getProductsInDeal() {
        var emptyCart = new ProductsInDeal();
        emptyCart.setAccount(new Account());
        emptyCart.setObjectId(new ObjectId());
        emptyCart.setProductInCartList(new ArrayList<ProductInCart>());
        return emptyCart;
    }

    @Bean
    @Scope("prototype")
    @Lazy
    public ProductInCart getProductInCart() {
        var productInCart = new ProductInCart();
        productInCart.setProduct(getProduct());
        productInCart.setRequiredQuantity(0);
        return productInCart;

    }

    @Bean
    @Scope("prototype")
    @Lazy
    public Product getProduct() {
        var product = new Product();
        product.setCategories(new ArrayList<Category>());
        product.setName(new String());
        product.setDescription(new String());
        product.setPrice(0.0);
        product.setObjectId(new ObjectId());
        product.setQuantity(0);
        return product;

    }

}