package com.lalit.user.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private Long authUserId;
    private String phone;
    private AddressDTO address;

}
