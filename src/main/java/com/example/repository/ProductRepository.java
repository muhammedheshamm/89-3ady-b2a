package com.example.repository;

import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public class ProductRepository extends MainRepository<Product> {
    
    public ProductRepository() {}

    @Value("${spring.application.productDataPath}")
    private String productDataPath;

    @Override
    protected String getDataPath() {
        return productDataPath;
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public Product addProduct(Product product) throws Exception {

        if(product == null) {
            throw new Exception("Product object is null");
        }

        // Check if a product with the same ID already exists
        for (Product p : findAll()) {
            if (p.getId().equals(product.getId())) {
                throw new Exception("Product Already Exists");
            }
        }

       // Save the new product
        save(product);
        return product;
    }

    // Retrieve all products
    public ArrayList<Product> getProducts() {
        return findAll();
    }

    public Product getProductById(UUID productId) throws Exception {
        for (Product product : findAll()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        throw new Exception("Product not found");
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) throws Exception {
        Product product = getProductById(productId);

        if (newName == null || newName.isEmpty()) {
            throw new Exception("Name cannot be empty");
        }

        if (newPrice < 0) {
            throw new Exception("Price cannot be negative");
        }

        // Update the product
        product.setName(newName);
        product.setPrice(newPrice);

        ArrayList<Product> products = findAll();
        products.remove(product);
        products.add(product);
        saveAll(products);
        return product;
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) throws Exception {
        if (discount < 0 || discount > 100) {
            throw new Exception("Discount must be between 0 and 100.");
        }

        ArrayList<Product> products = findAll();
        for (UUID productId : productIds) {
            Product product = getProductById(productId);
            double newPrice = product.getPrice() * (1 - discount / 100);
            product.setPrice(newPrice);
            products.remove(product);
            products.add(product);
        }

        saveAll(products); // Save the updated prices
    }

    public void deleteProductById(UUID productId) throws Exception {
        Product product = getProductById(productId); // Retrieve product by ID
        ArrayList<Product> products = findAll(); // Retrieve all products
        products.remove(product); // Remove product from list
        saveAll(products);
    }
}
