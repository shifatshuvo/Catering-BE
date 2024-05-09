package com.example.catering_service_practice.controller.dto;

import com.example.catering_service_practice.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignInResponseDto {
    private User user;
    private String tokenStr;
}
