package com.vodafone.ebuisness.model.main;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Category {

    @Id
    private ObjectId _id;

    private String name;
    private String description;
    private List<String> subscribers;

}
