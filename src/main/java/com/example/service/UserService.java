package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User>{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, OrderRepository orderRepository, CartService cartService) {
        super();
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {
        Cart userCart = cartService.getCartByUserId(userId);
        if (userCart == null || userCart.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or does not exist.");
        }

        double totalPrice = userCart.getProducts().stream().mapToDouble(Product::getPrice).sum();
        Order newOrder = new Order(UUID.randomUUID(), userId, totalPrice, new ArrayList<>(userCart.getProducts()));

        orderRepository.addOrder(newOrder);
        userRepository.addOrderToUser(userId, newOrder);
        cartService.emptyCart(userId);
    }

    public void emptyCart(UUID userId) {
        cartService.emptyCart(userId);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
        orderRepository.deleteOrderById(orderId);
    }

    public void deleteUserById(UUID userId) {
        cartService.deleteCartById(userId);
        userRepository.deleteUserById(userId);
    }
}