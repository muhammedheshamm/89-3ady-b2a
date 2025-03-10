package com.example.service;

import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ProductService extends MainService<Product>  {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CartRepository cartRepository) {
        super();
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public Product addProduct(Product product) throws Exception {
        return productRepository.addProduct(product);
    }

    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) throws Exception {
        return productRepository.getProductById(productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) throws Exception {
        Product newProduct = productRepository.updateProduct(productId, newName, newPrice);

        // Update the product in the cart
        for (UUID cartId : cartRepository.getCarts().stream().map(cart -> cart.getId()).toList()) {
            Product oldProduct = productRepository.getProductById(productId);
            cartRepository.deleteProductFromCart(cartId, oldProduct);
            cartRepository.addProductToCart(cartId, newProduct);
        }

        return newProduct;
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) throws Exception {
        productRepository.applyDiscount(discount, productIds);

        // Update the products in all carts with the new discounted prices
        for (UUID cartId : cartRepository.getCarts().stream().map(cart -> cart.getId()).toList()) {
            for (UUID productId : productIds) {
                Product updatedProduct = productRepository.getProductById(productId);
                cartRepository.deleteProductFromCart(cartId, updatedProduct);
                cartRepository.addProductToCart(cartId, updatedProduct);
            }
        }
    }

    public void deleteProductById(UUID productId) throws Exception {

        // Remove the product from the cart
        for (UUID cartId : cartRepository.getCarts().stream().map(cart -> cart.getId()).toList()) {
            Product product = productRepository.getProductById(productId);
            cartRepository.deleteProductFromCart(cartId, product);
        }

        productRepository.deleteProductById(productId);
    }
}
