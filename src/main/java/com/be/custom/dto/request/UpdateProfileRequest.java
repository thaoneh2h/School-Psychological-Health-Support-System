package com.be.custom.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
}
