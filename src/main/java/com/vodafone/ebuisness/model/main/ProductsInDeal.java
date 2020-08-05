package com.vodafone.ebuisness.model.main;

import com.vodafone.ebuisness.model.auxiliarymodel.ProductInCart;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collation = "Products_in_deal")
public class ProductsInDeal {

    @Id
    private ObjectId _id;

    private List<ProductInCart> productInCartList;

    @DBRef
    private Account account;

    @DBRef
    private Payment payment;

}
