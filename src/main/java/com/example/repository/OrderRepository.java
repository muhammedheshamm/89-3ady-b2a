package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/orders.json";
    }

    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }

    public OrderRepository() {
    }
    public void addOrder(Order order) {
        ArrayList<Order> orders = findAll(); // Retrieve existing orders

        // Ensure the order has a unique ID
//        if (order.getId() == null) {
//            order.setId(UUID.randomUUID()); // Assign a new UUID if missing
//        }

        orders.add(order); // Add the new order to the list
        saveAll(orders); // Save updated orders list back to JSON
    }

    public  ArrayList<Order> getOrders() {
        return findAll(); // Retrieve all orders from orders.json
    }

    public Order getOrderById(UUID orderId) {
        for (Order order : findAll()) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        throw new RuntimeException("Order with ID " + orderId + " not found.");
    }

    public void deleteOrderById(UUID orderId) {
        ArrayList<Order> orders = findAll();

        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                orders.remove(order);
                saveAll(orders);
                System.out.println("Order deleted: " + orderId);
                return;
            }
        }

        throw new RuntimeException("Order with ID " + orderId + " not found.");
    }

}
