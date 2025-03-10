package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class OrderService extends MainService<Order> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        super();
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public void addOrder(Order order) throws Exception {
        orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public Order getOrderById(UUID orderId) throws Exception {
        return orderRepository.getOrderById(orderId);
    }

    public void deleteOrderById(UUID orderId) throws Exception {
        orderRepository.deleteOrderById(orderId);
        for (UUID userId : userRepository.getUsers().stream().map(user -> user.getId()).toList()) {
            userRepository.removeOrderFromUser(userId, orderId);
        }
    }
}

