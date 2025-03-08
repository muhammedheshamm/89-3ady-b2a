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
@RequestMapping("/cart")
public class CartController {

  private final UserService userService;
  private final CartService cartService;
  private final ProductService productService;

  @Autowired
  public CartController(UserService userService, CartService cartService, ProductService productService) {
      this.userService = userService;
      this.cartService = cartService;
      this.productService = productService;
  }

  @PostMapping("/")
  public Cart addCart(@RequestBody Cart cart) throws Exception {
      return cartService.addCart(cart);
  }

  @GetMapping("/")
  public ArrayList<Cart> getCarts() {
      return cartService.getCarts();
  }

  @GetMapping("/{cartId}")
  public Cart getCartById(@PathVariable UUID cartId) throws Exception {
      return cartService.getCartById(cartId);
  }

  @PutMapping("/addProduct/{cartId}")
  public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product) {
      try {
          cartService.addProductToCart(cartId, product);
          return "Product added successfully";
      } catch (Exception e) {
          return e.getMessage();
      }
  }

  @DeleteMapping("/delete/{cartId}")
  public String deleteCartById(@PathVariable UUID cartId) {
      try {
          cartService.deleteCartById(cartId);
          return "Cart deleted successfully";
      } catch (Exception e) {
          return e.getMessage();
      }
  }

}
