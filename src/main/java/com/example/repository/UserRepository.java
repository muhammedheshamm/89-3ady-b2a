package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {

    public UserRepository() {
    }

    @Override
    protected String getDataPath() {
        return "users.json"; // Path to JSON storage
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class; // Define correct deserialization type
    }

    // Retrieve all users
    public ArrayList<User> getUsers() {
        return findAll();
    }

    // Retrieve a user by ID
    public User getUserById(UUID userId) {
        for (User user : findAll()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    // Add a new user
    public User addUser(User user) {
        ArrayList<User> users = findAll();

        // Generate a new UUID if not provided
//        if (user.getId() == null) {
//            user.setId(UUID.randomUUID());
//        }

        // Check if user ID already exists
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                throw new RuntimeException("User with ID " + user.getId() + " already exists.");
            }
        }

        users.add(user);
        saveAll(users);
        return user;
    }




    // 6.2.2.4 Get The Orders of a User
    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId); // Retrieve user by ID
        if (user == null) {
            throw new RuntimeException("User with ID " + userId + " not found.");
        }
        return user.getOrders();
    }

    // 6.2.2.5 Add Order to the User
    public void addOrderToUser(UUID userId, Order order) {
        ArrayList<User> users = findAll(); // Load all users
        for (User user : users) {
            if (user.getId().equals(userId)) {
//                if (order.getId() == null) {
//                    order.setId(UUID.randomUUID()); // Ensure order has a unique ID
//                }
                user.getOrders().add(order); // Add the order to the user's order list
                saveAll(users); // Save updated user data back to users.json
                System.out.println("Order added successfully for user " + userId);
                return;
            }
        }
        throw new RuntimeException("User with ID " + userId + " not found.");
    }


    // 6.2.2.6 Remove Order from User
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        ArrayList<User> users = findAll(); // Load all users

        for (User user : users) {
            if (user.getId().equals(userId)) {
                for (Order order : user.getOrders()) {
                    if (order.getId().equals(orderId)) {
                        user.getOrders().remove(order);
                        saveAll(users); // Save updated user data back to users.json
                        System.out.println("Order with ID " + orderId + " removed successfully for user " + userId);
                        return;
                    }
                }
                throw new RuntimeException("Order with ID " + orderId + " not found for user " + userId);
            }
        }

        throw new RuntimeException("User with ID " + userId + " not found.");
    }





    public void deleteUserById(UUID userId) {
        ArrayList<User> users = findAll();

        for (User user : users) {
            if (user.getId().equals(userId)) {
                users.remove(user);
                saveAll(users);
                return;
            }
        }

        throw new RuntimeException("User with ID " + userId + " not found.");
    }


}
