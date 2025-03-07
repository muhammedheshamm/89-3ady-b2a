package com.example.service;


import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product>  {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        super();
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.addProduct(product);
    }

    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) {
        return productRepository.getProductById(productId);
    }
    public Product updateProduct(UUID productId, String newName, double newPrice) {
        //unsure of whether logic should be here wala fel repo
        Product product = productRepository.getProductById(productId);
        if (product != null) {
            product.setName(newName);
            product.setPrice(newPrice);
            return productRepository.addProduct(product);
        }
        throw new IllegalArgumentException("Product not found.");
    }
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
       //same here as well
    }
}
