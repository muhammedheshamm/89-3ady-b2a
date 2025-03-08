package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public class OrderRepository extends MainRepository<Order> {
    
    public OrderRepository() {}

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/orders.json";
    }

    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }

    public void addOrder(Order order) throws Exception {
        // check if order ID already exists
        for (Order o : findAll()) {
            if (o.getId().equals(order.getId())) {
                throw new Exception("Order Already Exists");
            }
        }

        save(order);
    }

    public ArrayList<Order> getOrders() {
        return findAll(); // Retrieve all orders from orders.json
    }

    public Order getOrderById(UUID orderId) throws Exception {
        for (Order order : findAll()) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        throw new Exception("Order not found");
    }

    public void deleteOrderById(UUID orderId) throws Exception {
        Order order = getOrderById(orderId);
        ArrayList<Order> orders = findAll();
        orders.remove(order);
        saveAll(orders);
    }

}
