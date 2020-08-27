package com.vodafone.ebuisness.model.auxiliary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vodafone.ebuisness.model.main.Product;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class ProductInCart {

    @DBRef
    private Product product;

    @JsonProperty("required_quantity")
    private Integer requiredQuantity;

    public ProductInCart() {
    }

    public ProductInCart(Product product, Integer requiredQuantity) {
        this.product = product;
        this.requiredQuantity = requiredQuantity;
    }

    @JsonProperty("in_stock")
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
