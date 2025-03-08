package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        super();
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart) {
        return cartRepository.addCart(cart);
    }

    public ArrayList<Cart> getCarts() {
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId) {
        return cartRepository.getCartById(cartId);
    }

    public Cart getCartByUserId(UUID userId) {
        return cartRepository.getCartByUserId(userId);
    }

    public void addProductToCart(UUID cartId, Product product) {
        Cart cart = cartRepository.getCartById(cartId);
        if (cart != null) {
            cart.getProducts().add(product);
            cartRepository.addCart(cart);
        } else {
            throw new IllegalArgumentException("Cart not found.");
        }
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        Cart cart = cartRepository.getCartById(cartId);
        if (cart != null) {
            cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
            cartRepository.addCart(cart);
        } else {
            throw new IllegalArgumentException("Cart not found.");
        }
    }

    public void emptyCart(UUID userId) {
        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart != null) {
            cart.getProducts().clear();
        }
    }

    public void deleteCartById(UUID cartId) {
        cartRepository.deleteCartById(cartId);
    }
}
