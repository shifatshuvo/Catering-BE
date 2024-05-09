package com.example.catering_service_practice.service;


import com.example.catering_service_practice.controller.dto.OrderRequest;
import com.example.catering_service_practice.controller.dto.OrderResponse;
import com.example.catering_service_practice.controller.dto.UserDto;
import com.example.catering_service_practice.exception.UserNotFoundException;
import com.example.catering_service_practice.model.Order;
import com.example.catering_service_practice.model.Package;
import com.example.catering_service_practice.model.User;
import com.example.catering_service_practice.repository.OrderRepository;
import com.example.catering_service_practice.repository.PackageRepository;
import com.example.catering_service_practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;


    //Stored an Order
    public String placeOrder(User authUser, OrderRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            return "User not found!";
        }
        Optional<Package> optionalPackage = packageRepository.findById(request.getPackageId());
        if (optionalPackage.isEmpty()) {
            return "Package not found";
        }

        int price = request.getQuantity() * optionalPackage.get().getPrice();

        // set fields
        Order order = new Order();
        order.setPrice(price);
        order.setQuantity(request.getQuantity());
        order.setPkg(optionalPackage.get());

        if (authUser.getRole().equals("ADMIN")) {
            order.setUser(optionalUser.get());
        } else {
            order.setUser(authUser);
        }
        orderRepository.save(order);
        return "Order Created!";
    }


    // Update order
    public void updateOrder(User authUser, Long id, OrderRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found!!");
        }
        Optional<Package> optionalPackage = packageRepository.findById(request.getPackageId());
        if (optionalPackage.isEmpty()) {
            throw new RuntimeException("Package not found");
        }
        int price = request.getQuantity() * optionalPackage.get().getPrice();

        Optional<Order> optionalOrder = orderRepository.findById(id);
        Long orderId = optionalOrder.get().getId();

        // set fields
        Order order = new Order();
        order.setId(orderId);
        order.setPrice(price);
        order.setQuantity(request.getQuantity());
        order.setPkg(optionalPackage.get());

        if (authUser.getRole().equals("ADMIN")) {
            order.setUser(authUser);
        } else {
            order.setUser(authUser);
        }
        orderRepository.save(order);
    }


    // get one order
    public OrderResponse getOneOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return new OrderResponse(order, new UserDto(order.getUser()));
        }
        return null;
    }


    // get specific user orders
    public List<OrderResponse> getAllOrderOfAUser(Long id) {
        List<Order> orders = orderRepository.getAllByUserId(id);

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponses.add(new OrderResponse(order, new UserDto(order.getUser())));
        }
        return orderResponses;
    }



    // get all orders
    public List<OrderResponse> getAllOrders(Authentication auth) {
        boolean isAdmin = isAdmin(auth.getAuthorities());
        List<Order> orders;
        if (isAdmin) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findAllByUserId(Long.parseLong(auth.getName()));
        }

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponses.add(new OrderResponse(order, new UserDto(order.getUser())));
        }
        return orderResponses;
    }


    // delete an order
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }


    // check user role
    private boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) return true;
        }
        return false;
    }
}
