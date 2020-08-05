package com.vodafone.ebuisness.model.main;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document
public class Payment {

    @Id
    private ObjectId _id;

    @DBRef
    @Field("payment_mean")
    private PaymentMean paymentMean;

}
