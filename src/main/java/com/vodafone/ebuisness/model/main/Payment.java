package com.vodafone.ebuisness.model.main;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.vodafone.ebuisness.model.auxiliary.TimeStamp;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Payment {

    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private ObjectId objectId;

    private TimeStamp timeStamp;
    private Double amount;

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
