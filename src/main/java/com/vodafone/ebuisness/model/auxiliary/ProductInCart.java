package com.vodafone.ebuisness.model.auxiliary;

import com.vodafone.ebuisness.model.main.Product;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class ProductInCart {

    @DBRef
    private Product product;
    private Integer requiredQuantity;

    public ProductInCart() {
    }

    public ProductInCart(Product product, Integer requiredQuantity) {
        this.product = product;
        this.requiredQuantity = requiredQuantity;
    }

    public Boolean isInStock(){

        return requiredQuantity <= product.getQuantity();

    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
}
