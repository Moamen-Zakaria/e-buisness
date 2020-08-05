package com.vodafone.ebuisness.model.auxiliarymodel;

import com.vodafone.ebuisness.model.main.Product;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class ProductInCart {

    @DBRef
    private Product product;
    private int requiredQuantity;

    public Boolean isInStock(){

        return requiredQuantity <= product.getQuantity();

    }

}
