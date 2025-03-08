package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json";
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public ProductRepository() {
    }
    public Product addProduct(Product product) {
        ArrayList<Product> products = findAll(); // Load all products

        // Assign a unique ID if not provided
//        if (product.getId() == null) {
//            product.setId(UUID.randomUUID());
//        }

        // Check if a product with the same ID already exists
        for (Product p : products) {
            if (p.getId().equals(product.getId())) {
                throw new RuntimeException("Product with ID " + product.getId() + " already exists.");
            }
        }

        products.add(product); // Add the new product
        saveAll(products); // Save the updated product list to JSON

        return product; // Return the newly added product
    }

    public ArrayList<Product> getProducts() {
        return findAll(); // Retrieve all products from the JSON file
    }

    public Product getProductById(UUID productId) {
        for (Product product : findAll()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        throw new RuntimeException("Product not found with ID: " + productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                saveAll(products); // Save the updated list
                return product;
            }
        }
        throw new RuntimeException("Product with ID " + productId + " not found.");
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }

        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * (1 - discount / 100);
                product.setPrice(newPrice);
            }
        }

        saveAll(products); // Save the updated prices
        System.out.println("Discount of " + discount + "% applied to selected products.");
    }

    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                products.remove(product);
                saveAll(products);
                System.out.println("Product with ID " + productId + " deleted successfully.");
                return;
            }
        }

        throw new RuntimeException("Product with ID " + productId + " not found.");
    }



}
