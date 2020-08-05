package com.vodafone.ebuisness.model.main;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Category {

    @Id
    private ObjectId _id;

    private String name;
    private String description;
    private List<String> subscribers;

    public Category() {
    }

    public Category( String name, String description, List<String> subscribers) {
        this.name = name;
        this.description = description;
        this.subscribers = subscribers;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers;
    }
}
