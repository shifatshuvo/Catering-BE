package com.example.catering_service_practice.controller;


import com.example.catering_service_practice.controller.dto.OrderRequest;
import com.example.catering_service_practice.controller.dto.OrderResponse;
import com.example.catering_service_practice.exception.BadRequestException;
import com.example.catering_service_practice.model.User;
import com.example.catering_service_practice.model.auth.AuthToken;
import com.example.catering_service_practice.repository.OrderRepository;
import com.example.catering_service_practice.repository.TokenRepository;
import com.example.catering_service_practice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = ("*"))
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    TokenRepository tokenRepository;


    // create an order
    @PostMapping
    private ResponseEntity<String> savePost(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody OrderRequest request) {
        User authUser = authenticate(token);
        String response = orderService.placeOrder(authUser, request);
        if (response.equals("Package not found") || response.equals("User not found!")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }


    // orders of an user
    @GetMapping("user-orders/{id}")
    public List<OrderResponse> getAllOrdersByUserRole(@PathVariable Long id) {
        return orderService.getAllOrderOfAUser(id);
    }


    // get all orders
    @GetMapping
    public List<OrderResponse> getAllOrders(Authentication auth) {
        return orderService.getAllOrders(auth);
    }


    // get on order
    @GetMapping("/{id}")
    public OrderResponse getOneOrder(@PathVariable Long id) {
        return orderService.getOneOrder(id);
    }


    // update an order
    @PutMapping("/{id}")
    public void updateOrder(@PathVariable Long id,
                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                            @RequestBody OrderRequest request) {
        User authUser = authenticate(token);
        orderService.updateOrder(authUser, id, request);
    }


    // delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long id) {
        try {
            orderService.deleteOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete order");
        }
    }


    // user check by token
    private User authenticate(String token) {
        Optional<AuthToken> optionalToken = tokenRepository.findByTokenStr(token);
        if (optionalToken.isEmpty()) {
            throw new BadRequestException("Invalid Token");
        }
        return optionalToken.get().getUser();
    }

}
