package com.example.catering_service_practice.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequest {

    private long userId;
    private int packageId;
    private int quantity;
}
