package com.vodafone.ebuisness.model.main;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Product {

    @Id
    private ObjectId _id;
    private String name;
    private String description;
    private int quantity;
    private float price;

    @DBRef
    private List<Category> categories;

    public int getQuantity() {
        return quantity;
    }
}
