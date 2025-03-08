package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;

  @Autowired
  public CartController(CartService cartService) {
      this.cartService = cartService;
  }

  @PostMapping("/")
  public Cart addCart(@RequestBody Cart cart) {
    try {
      return cartService.addCart(cart);
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return null;
    }
  }

  @GetMapping("/")
  public ArrayList<Cart> getCarts() {
      return cartService.getCarts();
  }

  @GetMapping("/{cartId}")
  public Cart getCartById(@PathVariable UUID cartId) {
    try {
        return cartService.getCartById(cartId);
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return null;
    }
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
