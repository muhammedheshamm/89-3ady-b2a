package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    public CartRepository(){
    }

    // Add New Cart
    public Cart addCart(Cart cart) {
        ArrayList<Cart> carts = findAll();

        // Generate a new UUID if not provided
//        if (cart.getId() == null) {
//            cart.setId(UUID.randomUUID());
//        }

        carts.add(cart);
        saveAll(carts);
        return cart;
    }

    // Get All Carts
    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    // Get Specific Cart by ID
    public Cart getCartById(UUID cartId) {
        for (Cart cart : findAll()) {
            if (cart.getId().equals(cartId)) {
                return cart;
            }
        }
        throw new RuntimeException("Cart not found with ID: " + cartId);
    }

    // Get User's Cart by User ID
    public Cart getCartByUserId(UUID userId) {
        for (Cart cart : findAll()) {
            if (cart.getUserId().equals(userId)) {
                return cart;
            }
        }
        throw new RuntimeException("No cart found for user ID: " + userId);
    }
    // Add Product to Cart
    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        Cart cart = getCartById(cartId);

        cart.getProducts().add(product);
        saveAll(carts);
    }

    // Delete Product from Cart
    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        Cart cart = getCartById(cartId);

        for (Product p : cart.getProducts()) {
            if (p.getId().equals(product.getId())) {
                cart.getProducts().remove(p);
                saveAll(carts); // Save updated carts list
                return;
            }
        }

        throw new RuntimeException("Product not found in cart.");
    }



    // Delete the Cart
    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = findAll();

        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                carts.remove(cart);
                saveAll(carts); // Save updated carts list
                return;
            }
        }

        throw new RuntimeException("Cart with ID " + cartId + " not found.");
    }



}
