package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CartService extends MainService<Cart> {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        super();
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart) throws Exception {
        return cartRepository.addCart(cart);
    }

    public ArrayList<Cart> getCarts() {
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId) throws Exception {
        return cartRepository.getCartById(cartId);
    }

    public Cart getCartByUserId(UUID userId) throws Exception {
        return cartRepository.getCartByUserId(userId);
    }

    public void addProductToCart(UUID cartId, Product product) throws Exception {
        cartRepository.addProductToCart(cartId, product);
    }

    public void deleteProductFromCart(UUID cartId, Product product) throws Exception {
        cartRepository.deleteProductFromCart(cartId, product);
    }

    public void deleteCartById(UUID cartId) throws Exception {
        cartRepository.deleteCartById(cartId);
    }
}
