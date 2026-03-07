package com.auth_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String phoneNumber;
    private String password;

}