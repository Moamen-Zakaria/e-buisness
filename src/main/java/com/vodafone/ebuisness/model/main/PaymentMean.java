package com.vodafone.ebuisness.model.main;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="payment_mean")
public class PaymentMean {

    @Id
    private ObjectId _id;

    //no data in this class as this class's data's form is not decided yet
    private String mean;

    public PaymentMean() {
    }

    public PaymentMean(String mean) {
        this.mean = mean;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
