package com.vodafone.ebuisness.model.main;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="payment_mean")
public class PaymentMean {

    //no data in this class as this class's data's form is not decided yet
    private String mean;

}
