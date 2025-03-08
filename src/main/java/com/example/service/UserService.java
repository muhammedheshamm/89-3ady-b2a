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
import java.util.stream.Collectors;

@Service
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

    public User addUser(User user) throws Exception {
        return userRepository.addUser(user);
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) throws Exception {
        return userRepository.getUserById(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId) throws Exception {
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) throws Exception {
        Cart userCart = cartService.getCartByUserId(userId);
        if (userCart == null || userCart.getProducts().isEmpty()) {
            throw new Exception ("Cart is empty or does not exist.");
        }

        double totalPrice = userCart.getProducts().stream().mapToDouble(Product::getPrice).sum();
        Order newOrder = new Order(UUID.randomUUID(), userId, totalPrice, new ArrayList<>(userCart.getProducts()));

        orderRepository.addOrder(newOrder);
        userRepository.addOrderToUser(userId, newOrder);
    }

    public void emptyCart(UUID userId) throws Exception {
        Cart userCart = cartService.getCartByUserId(userId);
        userCart.setProducts(new ArrayList<>());
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) throws Exception {
        userRepository.removeOrderFromUser(userId, orderId);
        orderRepository.deleteOrderById(orderId);
    }

    public void deleteUserById(UUID userId) throws Exception {
        getOrdersByUserId(userId).forEach(order -> {
            try {
                removeOrderFromUser(userId, order.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        try {
            Cart userCart = cartService.getCartByUserId(userId);
            cartService.deleteCartById(userCart.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        userRepository.deleteUserById(userId);
    }
}