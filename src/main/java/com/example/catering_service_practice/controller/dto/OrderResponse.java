package com.example.catering_service_practice.controller.dto;


import com.example.catering_service_practice.model.Order;
import com.example.catering_service_practice.model.Package;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class OrderResponse {

    private Long id;
    @JsonProperty("package")
    private Package pkg;
    private UserDto user;
    private int quantity;
    private int price;

    public OrderResponse(Order order, UserDto userDto) {
        this.id = order.getId();
        this.pkg = order.getPkg();
        this.user = userDto;
        this.quantity = order.getQuantity();
        this.price = order.getPrice();
    }
}
