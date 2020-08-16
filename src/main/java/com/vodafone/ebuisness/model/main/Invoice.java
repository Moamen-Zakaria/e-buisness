package com.vodafone.ebuisness.model.main;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Invoice {

    @Id
    private String id;

    private String status;
//    private TimeStamp timeStamp;
//    private Double amount;

    public Invoice() {
    }

    public Invoice(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
