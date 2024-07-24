package com.TheMFG.E_Commerce.model.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String paymentType;
}
