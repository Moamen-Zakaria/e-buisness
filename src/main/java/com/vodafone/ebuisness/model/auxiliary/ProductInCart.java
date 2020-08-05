package com.vodafone.ebuisness.model.auxiliary;

import com.vodafone.ebuisness.model.main.Product;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class ProductInCart {

    @DBRef
    private Product product;
    private int requiredQuantity;

    public ProductInCart() {
    }

    public ProductInCart(Product product, int requiredQuantity) {
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

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
}
