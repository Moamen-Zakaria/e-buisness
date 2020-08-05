package com.vodafone.ebuisness.model.auxiliarymodel;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class PersonName {

    @Field("first_name")
    private String firstName;

    @Field("middle_name")
    private String middleName;

    @Field("last_name")
    private String lastName;

}
