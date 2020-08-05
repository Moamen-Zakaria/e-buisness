package com.vodafone.ebuisness.model.main;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Payment {

    @Id
    private ObjectId _id;

    @DBRef
    @Field("payment_mean")
    private PaymentMean paymentMean;

    public Payment() {
    }

    public Payment(PaymentMean paymentMean) {
        this.paymentMean = paymentMean;
    }

    public PaymentMean getPaymentMean() {
        return paymentMean;
    }

    public void setPaymentMean(PaymentMean paymentMean) {
        this.paymentMean = paymentMean;
    }
}
