package com.vodafone.ebuisness.model.auxiliarymodel;

import lombok.Data;

@Data
public class Address {

    private int blockNo;
    private int postalCode;
    private String street;
    private String district;
    private int departmentNo;
    private int floor;
    private String country;
    private String city;
    private String province;

}
