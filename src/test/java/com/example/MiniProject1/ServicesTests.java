package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class ServicesTests {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.application.userDataPath}")
    private String userDataPath;

    @Value("${spring.application.productDataPath}")
    private String productDataPath;

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath;

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    public void overRideAll(){
        try{
            objectMapper.writeValue(new File(userDataPath), new ArrayList<User>());
            objectMapper.writeValue(new File(productDataPath), new ArrayList<Product>());
            objectMapper.writeValue(new File(orderDataPath), new ArrayList<Order>());
            objectMapper.writeValue(new File(cartDataPath), new ArrayList<Cart>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public Object find(String typeString, Object toFind){
        switch(typeString){
            case "User":
                ArrayList<User> users = getUsers();

                for(User user: users){
                    if(user.getId().equals(((User)toFind).getId())){
                        return user;
                    }
                }
                break;
            case "Product":
                ArrayList<Product> products = getProducts();
                for(Product product: products){
                    if(product.getId().equals(((Product)toFind).getId())){
                        return product;
                    }
                }
                break;
            case "Order":
                ArrayList<Order> orders = getOrders();
                for(Order order: orders){
                    if(order.getId().equals(((Order)toFind).getId())){
                        return order;
                    }
                }
                break;
            case "Cart":
                ArrayList<Cart> carts = getCarts();
                for(Cart cart: carts){
                    if(cart.getId().equals(((Cart)toFind).getId())){
                        return cart;
                    }
                }
                break;
        }
        return null;
    }

    public Product addProduct(Product product) {
        try {
            File file = new File(productDataPath);
            ArrayList<Product> products;
            if (!file.exists()) {
                products = new ArrayList<>();
            }
            else {
                products = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
            }
            products.add(product);
            objectMapper.writeValue(file, products);
            return product;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }
    public ArrayList<Product> getProducts() {
        try {
            File file = new File(productDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Product>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    public User addUser(User user) {
        try {
            File file = new File(userDataPath);
            ArrayList<User> users;
            if (!file.exists()) {
                users = new ArrayList<>();
            }
            else {
                users = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
            }
            users.add(user);
            objectMapper.writeValue(file, users);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }
    public ArrayList<User> getUsers() {
        try {
            File file = new File(userDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<User>(Arrays.asList(objectMapper.readValue(file, User[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }
    public Cart addCart(Cart cart){
        try{
            File file = new File(cartDataPath);
            ArrayList<Cart> carts;
            if (!file.exists()) {
                carts = new ArrayList<>();
            }
            else {
                carts = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
            }
            carts.add(cart);
            objectMapper.writeValue(file, carts);
            return cart;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }
    public ArrayList<Cart> getCarts() {
        try {
            File file = new File(cartDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Cart>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }
    public Order addOrder(Order order){
        try{
            File file = new File(orderDataPath);
            ArrayList<Order> orders;
            if (!file.exists()) {
                orders = new ArrayList<>();
            }
            else {
                orders = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
            }
            orders.add(order);
            objectMapper.writeValue(file, orders);
            return order;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }
    public ArrayList<Order> getOrders() {
        try {
            File file = new File(orderDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Order>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    @BeforeEach
    void setUp() {
        overRideAll();
    }

    /*
     * UserService Tests
     */

    // addUser tests

    @Test
    void addUser_withValidInput_shouldReturnSameIdAndName() throws Exception {
        UUID userId = UUID.randomUUID();
        String name = "John Doe";
        User user = new User(userId, name, new ArrayList<>());
        userService.addUser(user);

        User createdUser = (User) find("User", user);

        assertNotNull(createdUser, "User should not be null");
        assertEquals(userId, createdUser.getId(), "User ID should be the same as the one provided");
        assertEquals(name, createdUser.getName(), "User name should be the same as the one provided");
    }

    @Test
    void addUser_withDuplicateId_shouldThrowException() throws Exception {
        UUID userId = UUID.randomUUID();
        String name1 = "John Doe";
        String name2 = "Jane Doe";

        User user1 = new User(userId, name1, new ArrayList<>());
        User user2 = new User(userId, name2, new ArrayList<>()); // Same ID, different name

        addUser(user1); // Add first user

        Exception exception = assertThrows(Exception.class, () -> userService.addUser(user2),
                "Adding a user with a duplicate ID should throw an exception");

        assertEquals("User Already Exists", exception.getMessage(),
                "Exception message should indicate duplicate user ID");
    }

    @Test
    void addUser_withNullObject_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> userService.addUser(null),
                "Adding a null user should throw an exception");

        assertEquals("User object is null", exception.getMessage(),
                "Exception message should indicate null user object");
    }

    // getUserById tests

    @Test
    void getUserById_withValidId_shouldReturnUser() throws Exception {
        UUID userId = UUID.randomUUID();
        String name = "John Doe";
        User user = new User(userId, name, new ArrayList<>());
        addUser(user);

        User retrievedUser = userService.getUserById(userId);

        assertEquals(userId, retrievedUser.getId(), "User ID should be the same as the one provided");
        assertEquals(name, retrievedUser.getName(), "User name should be the same as the one provided");
    }

    @Test
    void getUserById_withInvalidId_shouldThrowException() {
        UUID userId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> userService.getUserById(userId),
                "Retrieving a user with an invalid ID should throw an exception");

        assertEquals("User not found", exception.getMessage(),
                "Exception message should indicate user not found");
    }

    @Test
    void getUserById_withNullId_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> userService.getUserById(null),
                "Retrieving a user with a null ID should throw an exception");

        assertEquals("User not found", exception.getMessage(),
                "Exception message should indicate user not found");
    }

    // getUsers tests

    @Test
    void getUsers_withNoUsers_shouldReturnEmptyList() {
        assertEquals(0, userService.getUsers().size(), "User list should be empty");
    }

    @Test
    void getUsers_withMultipleUsers_shouldReturnAllUsers() throws Exception {
        User user1 = new User(UUID.randomUUID(), "John Doe", new ArrayList<>());
        User user2 = new User(UUID.randomUUID(), "Jane Doe", new ArrayList<>());
        addUser(user1);
        addUser(user2);

        ArrayList<User> users = userService.getUsers();

        assertEquals(2, users.size(), "User list should contain two users");
    }

    @Test
    void getUsers_withMultipleUsers_shouldReturnCorrectUsers() throws Exception {
        User user1 = new User(UUID.randomUUID(), "John Doe", new ArrayList<>());
        User user2 = new User(UUID.randomUUID(), "Jane Doe", new ArrayList<>());
        addUser(user1);
        addUser(user2);

        ArrayList<User> users = userService.getUsers();

        assertEquals(user1.getId(), users.get(0).getId(), "First user ID should match");
        assertEquals(user1.getName(), users.get(0).getName(), "First user name should match");

        assertEquals(user2.getId(), users.get(1).getId(), "Second user ID should match");
        assertEquals(user2.getName(), users.get(1).getName(), "Second user name should match");
    }

    // deleteUserById tests

    @Test
    void deleteUserById_withValidId_shouldDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe", new ArrayList<>());
        addUser(user);

        userService.deleteUserById(userId);

        assertEquals(null, find("User", user), "User should be deleted");
    }

    @Test
    void deleteUserById_withInvalidId_shouldThrowException() {
        UUID userId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> userService.deleteUserById(userId),
                "Deleting a user with an invalid ID should throw an exception");

        assertEquals("User not found", exception.getMessage(),
                "Exception message should indicate user not found");

    }

    @Test
    void deleteUserById_shouldDeleteUserCart() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe", new ArrayList<>());
        addUser(user);

        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        addCart(cart);

        userService.deleteUserById(userId);

        assertEquals(null, find("Cart", cart), "User's cart should be deleted");
    }

    // getOrdersByUserId tests

    @Test
    void getOrdersByUserId_withValidId_shouldReturnOrders() throws Exception {
        UUID userId = UUID.randomUUID();

        Order order1 = new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>());
        Order order2 = new Order(UUID.randomUUID(), userId, 200.0, new ArrayList<>());
        User user = new User(userId, "John Doe", new ArrayList<>(Arrays.asList(order1, order2)));

        addOrder(order1);
        addOrder(order2);
        addUser(user);

        List<Order> orders = userService.getOrdersByUserId(userId);

        assertEquals(2, orders.size(), "User should have two orders");
        assertEquals(order1.getId(), orders.get(0).getId(), "First order ID should match");
        assertEquals(order2.getId(), orders.get(1).getId(), "Second order ID should match");
    }

    @Test
    void getOrdersByUserId_withInvalidId_shouldThrowException() {
        UUID userId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> userService.getOrdersByUserId(userId),
                "Retrieving orders with an invalid user ID should throw an exception");

        assertEquals("User not found", exception.getMessage(),
                "Exception message should indicate user not found");
    }

    @Test
    void getOrdersByUserId_withNoOrders_shouldReturnEmptyList() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe", new ArrayList<>());
        addUser(user);

        List<Order> orders = userService.getOrdersByUserId(userId);

        assertEquals(0, orders.size(), "User should have no orders");
    }

    // addOrderToUser tests

    @Test
    void addOrderToUser_withValidUserIdAndCart_shouldAddOrder() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe", new ArrayList<>());
        addUser(user);

        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>(Arrays.asList(
                new Product(UUID.randomUUID(), "Product 1", 100.0),
                new Product(UUID.randomUUID(), "Product 2", 200.0)
        )));
        addCart(cart);

        userService.addOrderToUser(userId);

        List<Order> orders = ((User) find("User", user)).getOrders();
        System.out.println(orders.size());

        assertEquals(1, orders.size(), "User should have one order");
        assertEquals(300.0, orders.get(0).getTotalPrice(), "Order total price should be correct");
        assertEquals(2, orders.get(0).getProducts().size(), "Order should have two products");
    }

    @Test
    void addOrderToUser_withEmptyCart_shouldThrowException() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe", new ArrayList<>());
        addUser(user);

        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        addCart(cart);

        Exception exception = assertThrows(Exception.class, () -> userService.addOrderToUser(userId),
                "Adding an order with an empty cart should throw an exception");

        assertEquals("Cart is empty", exception.getMessage(),
                "Exception message should indicate empty cart");
    }

    @Test
    void addOrderToUser_withInvalidUserId_shouldThrowException() {
        UUID userId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> userService.addOrderToUser(userId),
                "Adding an order with an invalid user ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate user not found");
    }

    // removeOrderFromUser tests

    @Test
    void removeOrderFromUser_withValidUserIdAndOrderId_shouldRemoveOrder() throws Exception {
        UUID userId = UUID.randomUUID();

        Order order = new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>());
        User user = new User(userId, "John Doe", new ArrayList<>(Arrays.asList(order)));
        addOrder(order);
        addUser(user);

        userService.removeOrderFromUser(userId, order.getId());

        List<Order> orders = ((User) find("User", user)).getOrders();
        assertEquals(0, orders.size(), "User should have no orders");
        assertEquals(null, find("Order", order), "Order should be deleted");
    }

    @Test
    void removeOrderFromUser_withInvalidUserId_shouldThrowException() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> userService.removeOrderFromUser(userId, orderId),
                "Removing an order with an invalid user ID should throw an exception");

        assertEquals("User not found", exception.getMessage(),
                "Exception message should indicate user not found");
    }

    @Test
    void removeOrderFromUser_withInvalidOrderId_shouldThrowException() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User(userId, "John Doe", new ArrayList<>());
        addUser(user);

        Exception exception = assertThrows(Exception.class, () -> userService.removeOrderFromUser(userId, orderId),
                "Removing an order with an invalid order ID should throw an exception");

        assertEquals("Order not found", exception.getMessage(),
                "Exception message should indicate order not found");
    }

    // emptyCart tests

    @Test
    void emptyCart_withValidUserId_shouldEmptyCart() throws Exception {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>(Arrays.asList(
                new Product(UUID.randomUUID(), "Product 1", 100.0),
                new Product(UUID.randomUUID(), "Product 2", 200.0)
        )));
        addCart(cart);

        userService.emptyCart(userId);

        assertEquals(0, ((Cart) find("Cart", cart)).getProducts().size(), "Cart should be empty");
    }

    @Test
    void emptyCart_withInvalidUserId_shouldThrowException() {
        UUID userId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> userService.emptyCart(userId),
                "Emptying a cart with an invalid user ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    @Test
    void emptyCart_withEmptyCart_shouldNotThrowException() throws Exception {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        addCart(cart);

        userService.emptyCart(userId);

        assertEquals(0, ((Cart) find("Cart", cart)).getProducts().size(), "Cart should be empty");
    }

    /*
     * OrderService Tests
     */

    // addOrder tests

    @Test
    void addOrder_withValidInput_shouldReturnSameIdAndTotalPrice() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        double totalPrice = 100.0;
        Order order = new Order(orderId, userId, totalPrice, new ArrayList<>());
        orderService.addOrder(order);

        Order createdOrder = (Order) find("Order", order);

        assertNotNull(createdOrder, "Order should be created");
        assertEquals(orderId, createdOrder.getId(), "Order ID should be the same as the one provided");
        assertEquals(totalPrice, createdOrder.getTotalPrice(), "Order total price should be the same as the one provided");
    }

    @Test
    void addOrder_withDuplicateId_shouldThrowException() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        double totalPrice1 = 100.0;
        double totalPrice2 = 200.0;

        Order order1 = new Order(orderId, userId1, totalPrice1, new ArrayList<>());
        Order order2 = new Order(orderId, userId2, totalPrice2, new ArrayList<>()); // Same ID, different user ID

        addOrder(order1); // Add first order

        Exception exception = assertThrows(Exception.class, () -> orderService.addOrder(order2),
                "Adding an order with a duplicate ID should throw an exception");

        assertEquals("Order Already Exists", exception.getMessage(),
                "Exception message should indicate duplicate order ID");
    }

    @Test
    void addOrder_withNullObject_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> orderService.addOrder(null),
                "Adding a null order should throw an exception");

        assertEquals("Order object is null", exception.getMessage(),
                "Exception message should indicate null order object");
    }

    // getOrderById tests

    @Test
    void getOrderById_withValidId_shouldReturnOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        double totalPrice = 100.0;
        Order order = new Order(orderId, userId, totalPrice, new ArrayList<>());
        addOrder(order);

        Order retrievedOrder = orderService.getOrderById(orderId);

        assertEquals(orderId, retrievedOrder.getId(), "Order ID should be the same as the one provided");
        assertEquals(userId, retrievedOrder.getUserId(), "Order user ID should be the same as the one provided");
        assertEquals(totalPrice, retrievedOrder.getTotalPrice(), "Order total price should be the same as the one provided");
    }

    @Test
    void getOrderById_withInvalidId_shouldThrowException() {
        UUID orderId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> orderService.getOrderById(orderId),
                "Retrieving an order with an invalid ID should throw an exception");

        assertEquals("Order not found", exception.getMessage(),
                "Exception message should indicate order not found");
    }

    @Test
    void getOrderById_withNullId_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> orderService.getOrderById(null),
                "Retrieving an order with a null ID should throw an exception");

        assertEquals("Order not found", exception.getMessage(),
                "Exception message should indicate order not found");
    }

    // getOrders tests

    @Test
    void getOrders_withNoOrders_shouldReturnEmptyList() {
        assertEquals(0, orderService.getOrders().size(), "Order list should be empty");
    }

    @Test
    void getOrders_withMultipleOrders_shouldReturnAllOrders() throws Exception {
        Order order1 = new Order(UUID.randomUUID(), UUID.randomUUID(), 100.0, new ArrayList<>());
        Order order2 = new Order(UUID.randomUUID(), UUID.randomUUID(), 200.0, new ArrayList<>());
        addOrder(order1);
        addOrder(order2);

        ArrayList<Order> orders = orderService.getOrders();

        assertEquals(2, orders.size(), "Order list should contain two orders");
    }

    @Test
    void getOrders_withMultipleOrders_shouldReturnCorrectOrders() throws Exception {
        Order order1 = new Order(UUID.randomUUID(), UUID.randomUUID(), 100.0, new ArrayList<>());
        Order order2 = new Order(UUID.randomUUID(), UUID.randomUUID(), 200.0, new ArrayList<>());
        addOrder(order1);
        addOrder(order2);

        ArrayList<Order> orders = orderService.getOrders();

        assertEquals(order1.getId(), orders.get(0).getId(), "First order ID should match");
        assertEquals(order1.getUserId(), orders.get(0).getUserId(), "First order user ID should match");
        assertEquals(order1.getTotalPrice(), orders.get(0).getTotalPrice(), "First order total price should match");

        assertEquals(order2.getId(), orders.get(1).getId(), "Second order ID should match");
        assertEquals(order2.getUserId(), orders.get(1).getUserId(), "Second order user ID should match");
        assertEquals(order2.getTotalPrice(), orders.get(1).getTotalPrice(), "Second order total price should match");
    }

    // deleteOrderById tests

    @Test
    void deleteOrderById_withValidId_shouldDeleteOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, UUID.randomUUID(), 100.0, new ArrayList<>());
        addOrder(order);

        orderService.deleteOrderById(orderId);

        assertEquals(null, find("Order", order), "Order should be deleted");
    }

    @Test
    void deleteOrderById_withInvalidId_shouldThrowException() {
        UUID orderId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> orderService.deleteOrderById(orderId),
                "Deleting an order with an invalid ID should throw an exception");

        assertEquals("Order not found", exception.getMessage(),
                "Exception message should indicate order not found");
    }

    @Test
    void deleteOrderById_shouldDeleteItFromUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, userId, 100.0, new ArrayList<>());
        User user = new User(userId, "John Doe", new ArrayList<>(Arrays.asList(order)));
        addOrder(order);
        addUser(user);

        orderService.deleteOrderById(orderId);

        assertEquals(null, find("Order", order), "Order should be deleted");
        assertEquals(0, ((User) find("User", user)).getOrders().size(), "User should have no orders");
    }

    /*
     * CartService Tests
     */

    // addCart tests

    @Test
    void addCart_withValidInput_shouldReturnSameIdAndUserId() throws Exception {
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(cartId, userId, new ArrayList<>());
        cartService.addCart(cart);

        Cart createdCart = (Cart) find("Cart", cart);

        assertNotNull(createdCart, "Cart should be created");
        assertEquals(cartId, createdCart.getId(), "Cart ID should be the same as the one provided");
        assertEquals(userId, createdCart.getUserId(), "Cart user ID should be the same as the one provided");
    }

    @Test
    void addCart_withDuplicateId_shouldThrowException() throws Exception {
        UUID cartId = UUID.randomUUID();
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        Cart cart1 = new Cart(cartId, userId1, new ArrayList<>());
        Cart cart2 = new Cart(cartId, userId2, new ArrayList<>()); // Same ID, different user ID

        addCart(cart1); // Add first cart

        Exception exception = assertThrows(Exception.class, () -> cartService.addCart(cart2),
                "Adding a cart with a duplicate ID should throw an exception");

        assertEquals("Cart Already Exists", exception.getMessage(),
                "Exception message should indicate duplicate cart ID");
    }

    @Test
    void addCart_withNullObject_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> cartService.addCart(null),
                "Adding a null cart should throw an exception");

        assertEquals("Cart object is null", exception.getMessage(),
                "Exception message should indicate null cart object");
    }

    // getCartById tests

    @Test
    void getCartById_withValidId_shouldReturnCart() throws Exception {
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(cartId, userId, new ArrayList<>());
        addCart(cart);

        Cart retrievedCart = cartService.getCartById(cartId);

        assertEquals(cartId, retrievedCart.getId(), "Cart ID should be the same as the one provided");
        assertEquals(userId, retrievedCart.getUserId(), "Cart user ID should be the same as the one provided");
    }

    @Test
    void getCartById_withInvalidId_shouldThrowException() {
        UUID cartId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> cartService.getCartById(cartId),
                "Retrieving a cart with an invalid ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    @Test
    void getCartById_withNullId_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> cartService.getCartById(null),
                "Retrieving a cart with a null ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    // getCartByUserId tests

    @Test
    void getCartByUserId_withValidId_shouldReturnCart() throws Exception {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        addCart(cart);

        Cart retrievedCart = cartService.getCartByUserId(userId);

        assertEquals(userId, retrievedCart.getUserId(), "Cart user ID should be the same as the one provided");
    }

    @Test
    void getCartByUserId_withInvalidId_shouldThrowException() {
        UUID userId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> cartService.getCartByUserId(userId),
                "Retrieving a cart with an invalid user ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    @Test
    void getCartByUserId_withInvalidUserId_shouldThrowException() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        addCart(cart);

        Exception exception = assertThrows(Exception.class, () -> cartService.getCartByUserId(userId),
                "Retrieving a cart with an invalid user ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    // getCarts tests

    @Test
    void getCarts_withNoCarts_shouldReturnEmptyList() {
        assertEquals(0, cartService.getCarts().size(), "Cart list should be empty");
    }

    @Test
    void getCarts_withMultipleCarts_shouldReturnAllCarts() throws Exception {
        Cart cart1 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        Cart cart2 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        addCart(cart1);
        addCart(cart2);

        ArrayList<Cart> carts = cartService.getCarts();

        assertEquals(2, carts.size(), "Cart list should contain two carts");
    }

    @Test
    void getCarts_withMultipleCarts_shouldReturnCorrectCarts() throws Exception {
        Cart cart1 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        Cart cart2 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        addCart(cart1);
        addCart(cart2);

        ArrayList<Cart> carts = cartService.getCarts();

        assertEquals(cart1.getId(), carts.get(0).getId(), "First cart ID should match");
        assertEquals(cart1.getUserId(), carts.get(0).getUserId(), "First cart user ID should match");

        assertEquals(cart2.getId(), carts.get(1).getId(), "Second cart ID should match");
        assertEquals(cart2.getUserId(), carts.get(1).getUserId(), "Second cart user ID should match");
    }

    // deleteCartById tests

    @Test
    void deleteCartById_withValidId_shouldDeleteCart() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        addCart(cart);

        cartService.deleteCartById(cartId);

        assertEquals(null, find("Cart", cart), "Cart should be deleted");
    }

    @Test
    void deleteCartById_withInvalid_Id_shouldThrowException() {
        UUID cartId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> cartService.deleteCartById(cartId),
                "Deleting a cart with an invalid ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    @Test
    void deleteCartById_withNullId_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> cartService.deleteCartById(null),
                "Deleting a cart with a null ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    // addProductToCart tests

    @Test
    void addProductToCart_withValidCartIdAndProduct_shouldAddProduct() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        addCart(cart);

        Product product = new Product(UUID.randomUUID(), "Product 1", 100.0);
        addProduct(product);

        cartService.addProductToCart(cartId, product);

        Cart updatedCart = (Cart) find("Cart", cart);

        assertEquals(1, updatedCart.getProducts().size(), "Cart should have one product");
        assertEquals(product.getId(), updatedCart.getProducts().get(0).getId(), "Product ID should match");
        assertEquals(product.getName(), updatedCart.getProducts().get(0).getName(), "Product name should match");
        assertEquals(product.getPrice(), updatedCart.getProducts().get(0).getPrice(), "Product price should match");
    }
    
    @Test
    void addProductToCart_withDuplicateProduct_shouldThrowException() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        addCart(cart);

        Product product = new Product(UUID.randomUUID(), "Product 1", 100.0);
        addProduct(product);

        cartService.addProductToCart(cartId, product);

        Exception exception = assertThrows(Exception.class, () -> cartService.addProductToCart(cartId, product),
                "Adding a duplicate product to a cart should throw an exception");

        assertEquals("Product already exists in cart.", exception.getMessage(),
                "Exception message should indicate duplicate product");
    }

    @Test
    void addProductToCart_withInvalidCartId_shouldThrowException() {
        UUID cartId = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Product 1", 100.0);
        addProduct(product);

        Exception exception = assertThrows(Exception.class, () -> cartService.addProductToCart(cartId, product),
                "Adding a product to a cart with an invalid ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    // deleteProductFromCart tests

    @Test
    void deleteProductFromCart_withValidCartIdAndProductId_shouldRemoveProduct() throws Exception {
        UUID cartId = UUID.randomUUID();
        Product product1 = new Product(UUID.randomUUID(), "Product 1", 100.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 200.0);
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>(Arrays.asList(
                product1, product2
        )));

        addCart(cart);


        cartService.deleteProductFromCart(cartId, product1);

        Cart updatedCart = (Cart) find("Cart", cart);

        assertEquals(1, updatedCart.getProducts().size(), "Cart should have one product");
        assertEquals("Product 2", updatedCart.getProducts().get(0).getName(), "Product name should match");
        assertEquals(200.0, updatedCart.getProducts().get(0).getPrice(), "Product price should match");
    }

    @Test
    void deleteProductFromCart_withInvalidCartId_shouldThrowException() {
        UUID cartId = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Product 1", 100.0);
        addProduct(product);

        Exception exception = assertThrows(Exception.class, () -> cartService.deleteProductFromCart(cartId, product),
                "Deleting a product from a cart with an invalid ID should throw an exception");

        assertEquals("Cart not found", exception.getMessage(),
                "Exception message should indicate cart not found");
    }

    @Test
    void deleteProductFromCart_fromEmptyCart_shouldThrowException() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        addCart(cart);

        Product product = new Product(UUID.randomUUID(), "Product 1", 100.0);
        addProduct(product);

        Exception exception = assertThrows(Exception.class, () -> cartService.deleteProductFromCart(cartId, product),
                "Deleting a product from an empty cart should throw an exception");

        assertEquals("Cart is empty", exception.getMessage(),
                "Exception message should indicate product not found");
    }


    /*
     * ProductService Tests
     */

    // addProduct tests

    @Test
    void addProduct_withValidInput_shouldReturnSameIdAndName() throws Exception {
        UUID productId = UUID.randomUUID();
        String name = "Product 1";
        double price = 100.0;
        Product product = new Product(productId, name, price);
        productService.addProduct(product);

        Product createdProduct = (Product) find("Product", product);

        assertNotNull(createdProduct, "Product should be created");
        assertEquals(productId, createdProduct.getId(), "Product ID should be the same as the one provided");
        assertEquals(name, createdProduct.getName(), "Product name should be the same as the one provided");
        assertEquals(price, createdProduct.getPrice(), "Product price should be the same as the one provided");
    }

    @Test
    void addProduct_withDuplicateId_shouldThrowException() throws Exception {
        UUID productId = UUID.randomUUID();
        String name1 = "Product 1";
        String name2 = "Product 2";
        double price1 = 100.0;
        double price2 = 200.0;

        Product product1 = new Product(productId, name1, price1);
        Product product2 = new Product(productId, name2, price2); // Same ID, different name

        addProduct(product1); // Add first product

        Exception exception = assertThrows(Exception.class, () -> productService.addProduct(product2),
                "Adding a product with a duplicate ID should throw an exception");

        assertEquals("Product Already Exists", exception.getMessage(),
                "Exception message should indicate duplicate product ID");
    }

    @Test
    void addProduct_withNullObject_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> productService.addProduct(null),
                "Adding a null product should throw an exception");

        assertEquals("Product object is null", exception.getMessage(),
                "Exception message should indicate null product object");
    }

    // getProductById tests

    @Test
    void getProductById_withValidId_shouldReturnProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        String name = "Product 1";
        double price = 100.0;
        Product product = new Product(productId, name, price);
        addProduct(product);

        Product retrievedProduct = productService.getProductById(productId);

        assertEquals(productId, retrievedProduct.getId(), "Product ID should be the same as the one provided");
        assertEquals(name, retrievedProduct.getName(), "Product name should be the same as the one provided");
        assertEquals(price, retrievedProduct.getPrice(), "Product price should be the same as the one provided");
    }

    @Test
    void getProductById_withInvalidId_shouldThrowException() {
        UUID productId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> productService.getProductById(productId),
                "Retrieving a product with an invalid ID should throw an exception");

        assertEquals("Product not found", exception.getMessage(),
                "Exception message should indicate product not found");
    }

    @Test
    void getProductById_withNullId_shouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> productService.getProductById(null),
                "Retrieving a product with a null ID should throw an exception");

        assertEquals("Product not found", exception.getMessage(),
                "Exception message should indicate product not found");
    }

    // getProducts tests

    @Test
    void getProducts_withNoProducts_shouldReturnEmptyList() {
        assertEquals(0, productService.getProducts().size(), "Product list should be empty");
    }

    @Test
    void getProducts_withMultipleProducts_shouldReturnAllProducts() throws Exception {
        Product product1 = new Product(UUID.randomUUID(), "Product 1", 100.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 200.0);
        addProduct(product1);
        addProduct(product2);

        ArrayList<Product> products = productService.getProducts();

        assertEquals(2, products.size(), "Product list should contain two products");
    }

    @Test
    void getProducts_withMultipleProducts_shouldReturnCorrectProducts() throws Exception {
        Product product1 = new Product(UUID.randomUUID(), "Product 1", 100.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 200.0);
        addProduct(product1);
        addProduct(product2);

        ArrayList<Product> products = productService.getProducts();

        assertEquals(product1.getId(), products.get(0).getId(), "First product ID should match");
        assertEquals(product1.getName(), products.get(0).getName(), "First product name should match");
        assertEquals(product1.getPrice(), products.get(0).getPrice(), "First product price should match");

        assertEquals(product2.getId(), products.get(1).getId(), "Second product ID should match");
        assertEquals(product2.getName(), products.get(1).getName(), "Second product name should match");
        assertEquals(product2.getPrice(), products.get(1).getPrice(), "Second product price should match");
    }

    // deleteProductById tests

    @Test
    void deleteProductById_withValidId_shouldDeleteProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product 1", 100.0);
        addProduct(product);

        productService.deleteProductById(productId);

        assertEquals(null, find("Product", product), "Product should be deleted");
    }

    @Test
    void deleteProductById_withInvalidId_shouldThrowException() {
        UUID productId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> productService.deleteProductById(productId),
                "Deleting a product with an invalid ID should throw an exception");

        assertEquals("Product not found", exception.getMessage(),
                "Exception message should indicate product not found");
    }

    @Test
    void deleteProductById_shouldDeleteItFromCart() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product 1", 100.0);
        addProduct(product);

        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>(Arrays.asList(product)));
        addCart(cart);

        productService.deleteProductById(productId);

        assertEquals(null, find("Product", product), "Product should be deleted");
        assertEquals(0, ((Cart) find("Cart", cart)).getProducts().size(), "Cart should have no products");
    }

    // UpdateProduct tests

    @Test
    void updateProduct_withValidInput_shouldUpdateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product 1", 100.0);
        addProduct(product);

        productService.updateProduct(productId, "Product 2", 200.0);

        Product retrievedProduct = (Product) find("Product", product);

        assertEquals(productId, retrievedProduct.getId(), "Product ID should be the same as the one provided");
        assertEquals("Product 2", retrievedProduct.getName(), "Product name should be updated");
        assertEquals(200.0, retrievedProduct.getPrice(), "Product price should be updated");
    }

    @Test
    void updateProduct_withInvalidId_shouldThrowException() {
        UUID productId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> productService.updateProduct(productId, "product 2", 200.0),
                "Updating a product with an invalid ID should throw an exception");

        assertEquals("Product not found", exception.getMessage(),
                "Exception message should indicate product not found");
    }

    @Test
    void updateProduct_shouldUpdateItInCart() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product 1", 100.0);
        addProduct(product);

        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>(Arrays.asList(product)));
        addCart(cart);

        productService.updateProduct(productId, "Product 2", 200.0);

        Product updatedProduct = productService.getProductById(productId);
        Cart updatedCart = (Cart) find("Cart", cart);

        assertEquals("Product 2", updatedProduct.getName(), "Product name should be updated");
        assertEquals(200.0, updatedProduct.getPrice(), "Product price should be updated");
        assertEquals("Product 2", updatedCart.getProducts().get(0).getName(), "Product name in cart should be updated");
        assertEquals(200.0, updatedCart.getProducts().get(0).getPrice(), "Product price in cart should be updated");
    }

    // ApplyDiscount tests

    @Test
    void applyDiscount_withValidInput_shouldApplyDiscountToAllInputProducts() throws Exception {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Product product1 = new Product(productId1, "Product 1", 100.0);
        Product product2 = new Product(productId2, "Product 2", 200.0);
        addProduct(product1);
        addProduct(product2);

        productService.applyDiscount(50, new ArrayList<>(Arrays.asList(productId1, productId2)));

        Product updatedProduct1 = productService.getProductById(productId1);
        Product updatedProduct2 = productService.getProductById(productId2);

        assertEquals(50.0, updatedProduct1.getPrice(), "Product 1 price should be updated");
        assertEquals(100.0, updatedProduct2.getPrice(), "Product 2 price should be updated");

        Exception exception = assertThrows(Exception.class, () -> productService.applyDiscount(500, new ArrayList<>(Arrays.asList(productId1, productId2))),
                "Discounting a product by more than 100% should throw an exception");
        
        assertEquals("Discount must be between 0 and 100.", exception.getMessage(),
                "Exception message should indicate discount cannot be more than 100%");

        Exception exception2 = assertThrows(Exception.class, () -> productService.applyDiscount(-50, new ArrayList<>(Arrays.asList(productId1, productId2))),
                "Discounting a product by a negative percentage should throw an exception");

        assertEquals("Discount must be between 0 and 100.", exception2.getMessage(),
                "Exception message should indicate discount cannot be negative");
    }

    @Test
    void applyDiscount_withInvalidId_shouldThrowException() {
        UUID productId = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class, () -> productService.applyDiscount(50, new ArrayList<>(Arrays.asList(productId))),
                "Applying a discount to a product with an invalid ID should throw an exception");

        assertEquals("Product not found", exception.getMessage(),
                "Exception message should indicate product not found");
    }

    @Test
    void applyDiscount_shouldUpdateItInCart() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product 1", 100.0);
        addProduct(product);

        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>(Arrays.asList(product)));
        addCart(cart);

        productService.applyDiscount(50, new ArrayList<>(Arrays.asList(productId)));

        Product updatedProduct = productService.getProductById(productId);
        Cart updatedCart = (Cart) find("Cart", cart);

        assertEquals(50.0, updatedProduct.getPrice(), "Product price should be updated");
        assertEquals(50.0, updatedCart.getProducts().get(0).getPrice(), "Product price in cart should be updated");

    }



}
