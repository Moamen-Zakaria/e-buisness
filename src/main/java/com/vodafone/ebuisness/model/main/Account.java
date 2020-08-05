package com.vodafone.ebuisness.model.main;

import com.vodafone.ebuisness.model.auxiliarymodel.Address;
import com.vodafone.ebuisness.model.auxiliarymodel.Date;
import com.vodafone.ebuisness.model.auxiliarymodel.PersonName;
import com.vodafone.ebuisness.model.auxiliarymodel.Role;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document
public class Account {

    @Id
    private ObjectId _id;

    @Field("date_of_birth")
    private Date dateOfBirth;

    private String email;
    private String username;
    private String password;
    private Address address;

    @Field("name")
    private PersonName personName;

    private List<String> roles;

    @DBRef
    private List<PaymentMean> paymentMeans;

}
