package com.example.controller;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public UserController(UserService userService, CartService cartService, ProductService productService) {
        this.userService = userService;
        this.cartService = cartService;
        this.productService = productService;
    }
    // Add a new user
    @PostMapping("/")
    public User addUser(@RequestBody User user) throws Exception {
        return userService.addUser(user);   
    }

    // Retrieve all users
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    // Retrieve a user by ID
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) throws Exception {
        return userService.getUserById(userId);
    }

    // Retrieve all orders by user ID
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) throws Exception {
        return userService.getOrdersByUserId(userId);
    }

    // Add an order to a user
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        try {
            userService.addOrderToUser(userId);
            return "Order added successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Remove an order from a user
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        try {
            userService.removeOrderFromUser(userId, orderId);
            return "Order removed successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Empty a user's cart
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return "Cart emptied successfully";
    }

    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = productService.getProductById(productId);
        cartService.addProductToCart(userId, product);
        return "Product added to cart successfully";
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = productService.getProductById(productId);
        cartService.deleteProductFromCart(userId, product);
        return "Product removed from cart successfully";
    }

    // Delete a user by ID
    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        try {
        userService.deleteUserById(userId);
        return "User deleted successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
