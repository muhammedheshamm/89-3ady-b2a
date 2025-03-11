package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository extends MainRepository<User> {

    public UserRepository() {}

    @Value("${spring.application.userDataPath}")
    private String userDataPath;

    @Override
    protected String getDataPath() {
        return userDataPath;
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
    public User getUserById(UUID userId) throws Exception {
        for (User user : findAll()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        throw new Exception("User not found");
    }

    // Add a new user
    public User addUser(User user) throws Exception {

        if (user == null) {
            throw new Exception("User object is null");
        }

        // Check if user ID already exists
        for (User u : findAll()) {
            if (u.getId().equals(user.getId())) {
                throw new Exception("User Already Exists");
            }
        }

        if(user.getOrders() == null) {
            user = new User(user.getName(), new ArrayList<Order>());
        }

        // Save the new user
        save(user);
        return user;
    }

    // 6.2.2.4 Get The Orders of a User
    public List<Order> getOrdersByUserId(UUID userId) throws Exception {
        User user = getUserById(userId); // Retrieve user by ID
        return user.getOrders();
    }

    // 6.2.2.5 Add Order to the User
    public void addOrderToUser(UUID userId, Order order) throws Exception {
        User user = getUserById(userId); // Retrieve user by ID
        List<Order> orders = user.getOrders(); // Add order to user
        orders.add(order);
        System.out.println("orders: " + orders.size());
        user.setOrders(orders); // Update user's orders
        System.out.println("user orders: " + user.getOrders().size());
        // get all users -> remove the user -> add the user with the new order -> save all
        ArrayList<User> users = findAll();
        users.remove(user);
        users.add(user);
        saveAll(users); // Save updated user data back to users.json
    }


    // 6.2.2.6 Remove Order from User
    public void removeOrderFromUser(UUID userId, UUID orderId) throws Exception {
        User user = getUserById(userId); // Retrieve user by ID
        List<Order> orders = user.getOrders(); // Retrieve user's orders

        // Remove order by ID
        orders.removeIf(order -> order.getId().equals(orderId));

        user.setOrders(orders); // Update user's orders
        // get all users -> remove the user -> add the user with the new order -> save all
        ArrayList<User> users = findAll();
        users.remove(user);
        users.add(user);
        saveAll(users); // Save updated user data back to users.json
    }

    public void deleteUserById(UUID userId) throws Exception {
        User user = getUserById(userId); // Retrieve user by ID
        ArrayList<User> users = findAll(); // Retrieve all users
        users.remove(user); // Remove user from list
        saveAll(users); // Save updated user list back to users.json
    }
}
