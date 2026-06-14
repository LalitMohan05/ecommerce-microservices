package com.lalit.user.dto;

import lombok.Data;
import com.lalit.user.enums.UserRole;

@Data
public class UserResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private AddressDTO address;
}
