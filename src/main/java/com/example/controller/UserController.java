package com.example.controller;

import com.example.model.Cart;
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
    public User addUser(@RequestBody User user) {
        try {
            return userService.addUser(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }   
    }

    // Retrieve all users
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    // Retrieve a user by ID
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        try {
            return userService.getUserById(userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Retrieve all orders by user ID
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        try {
            return userService.getOrdersByUserId(userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
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
        try {
            userService.emptyCart(userId);
            return "Cart emptied successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            Product product = productService.getProductById(productId);
            cartService.addProductToCart(cart.getId(), product);
            return "Product added to cart";
        } catch (Exception e) {
            if (e.getMessage().equals("Cart not found")) {
                try {
                    cartService.addCart(new Cart(UUID.randomUUID(), userId, new ArrayList<>()));
                    Cart cart = cartService.getCartByUserId(userId);
                    Product product = productService.getProductById(productId);
                    cartService.addProductToCart(cart.getId(), product);
                    return "Product added to cart";
                } catch (Exception ex) {
                    return ex.getMessage();
                }
            } else {
                return e.getMessage();
            }
        }
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            Product product = productService.getProductById(productId);
            Cart cart = cartService.getCartByUserId(userId);
            cartService.deleteProductFromCart(cart.getId(), product);
            return "Product deleted from cart";
        } catch (Exception e) {
            if (e.getMessage().equals("Cart not found")) {
                try {
                    cartService.addCart(new Cart(UUID.randomUUID(), userId, new ArrayList<>()));
                    Cart cart = cartService.getCartByUserId(userId);
                    Product product = productService.getProductById(productId);
                    cartService.deleteProductFromCart(cart.getId(), product);
                    return "Product deleted from cart";
                } catch (Exception ex) {
                    return ex.getMessage();
                }
            } else {
                return e.getMessage();
            }
        }
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
