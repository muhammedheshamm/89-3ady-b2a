package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CartRepository extends MainRepository<Cart> {

    public CartRepository() {}

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    @Override
    protected String getDataPath() {
        return cartDataPath;
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    // Add New Cart
    public Cart addCart(Cart cart) throws Exception {
        
        if (cart == null) {
            throw new Exception("Cart object is null");
        }

        // Check if cart ID already exists
        for (Cart c : findAll()) {
            if (c.getId().equals(cart.getId())) {
                throw new Exception("Cart Already Exists");
            }
        }

        // Save the new cart
        save(cart);
        return cart;
    }

    // Get All Carts
    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    // Get Specific Cart by ID
    public Cart getCartById(UUID cartId) throws Exception {
        for (Cart cart : findAll()) {
            if (cart.getId().equals(cartId)) {
                return cart;
            }
        }
        throw new Exception("Cart not found");
    }

    // Get User's Cart by User ID
    public Cart getCartByUserId(UUID userId) throws Exception {
        for (Cart cart : findAll()) {
            if (cart.getUserId().equals(userId)) {
                return cart;
            }
        }
        throw new Exception("Cart not found");
    }

    // Add Product to Cart
    public void addProductToCart(UUID cartId, Product product) throws Exception {
        Cart cart = getCartById(cartId);

        for (Product p : cart.getProducts()) {
            if (p.getId().equals(product.getId())) {
                throw new Exception("Product already exists in cart.");
            }
        }

        List <Product> products = cart.getProducts();
        products.add(product);
        cart.setProducts(products);

        ArrayList<Cart> carts = findAll();
        carts.remove(cart);
        carts.add(cart);
        saveAll(carts);
    }

    // Delete Product from Cart
    public void deleteProductFromCart(UUID cartId, Product product) throws Exception {
        Cart cart = getCartById(cartId);

        if(cart.getProducts().isEmpty()) {
            throw new Exception("Cart is empty");
        }

        for (Product p : cart.getProducts()) {
            if (p.getId().equals(product.getId())) {
                List<Product> products = cart.getProducts();
                products.remove(p);
                cart.setProducts(products);
                ArrayList<Cart> carts = findAll();
                carts.remove(cart);
                carts.add(cart);
                saveAll(carts);
                return;
            }
        }

        throw new Exception("Product not found in cart");
    }

    // Delete the Cart
    public void deleteCartById(UUID cartId) throws Exception {
        ArrayList<Cart> carts = findAll();
        Cart cart = getCartById(cartId);
        carts.remove(cart);
        saveAll(carts);
    }

}
