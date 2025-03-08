package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        try {
            return productService.addProduct(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/")
    public ArrayList<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId) {
        try {
            return productService.getProductById(productId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String,Object> body) {
        try {
            String newName = (String) body.get("newName");
            double newPrice = (double) body.get("newPrice");
            return productService.updateProduct(productId, newName, newPrice);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount,@RequestBody ArrayList<UUID> productIds) {
        try {
            productService.applyDiscount(discount, productIds);
            return "Discount applied successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId) {
        try {
            productService.deleteProductById(productId);
            return "Product deleted successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
