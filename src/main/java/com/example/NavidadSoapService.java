package com.example;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// ==========================
// DTOs
// ==========================
class Customer {
    private Long customerId;
    private String name;
    private String email;

    public Customer() {}

    public Customer(Long customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", name=" + name + ", email=" + email + '\'' + '}';
    }
}

class Gift {
    private Long giftId;
    private String name;
    private Double price;
    private Integer inStock;

    public Gift()
    {
    }

    public Gift(Long giftId, String name, Double price, Integer inStock) {
        this.giftId = giftId;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
    }

    public Long getGiftId() { return giftId; }
    public void setGiftId(Long giftId) { this.giftId = giftId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getInStock() { return inStock; }
    public void setInStock(Integer inStock) { this.inStock = inStock; }

    @Override
    public String toString() {
        return "Gift{" + "giftId=" + giftId + ", name='" + name + '\'' + ", price=" + price + ", inStock=" + inStock + '}';
    }
}

class OrderDTO {
    private Long orderId;
    private Long customerId;
    private Long giftId;
    private Integer quantity;
    private Double totalPrice;

    public OrderDTO() {}

    public OrderDTO(Long orderId, Long customerId, Long giftId, Integer quantity, Double totalPrice)
    {
        this.orderId = orderId;
        this.customerId = customerId;
        this.giftId = giftId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getGiftId() { return giftId; }
    public void setGiftId(Long giftId) { this.giftId = giftId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return "OrderDTO{" + "orderId=" + orderId + ", customerId=" + customerId + ", giftId=" + giftId + ", quantity=" + quantity + ", totalPrice=" + totalPrice + '}';
    }
}

// ==========================
// DAO (Oracle JDBC)
// ==========================
class OraclDAO {
    // ajusta estos datos a tu entorno
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String CONNECTION_STRING = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String USER = "system";
    private static final String PASS = "Tapiero123";

    public OraclDAO() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC driver not found. Añade ojdbc jar al classpath.", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, USER, PASS);
    }

    // customer CRUD
    public Customer createCustomer(Customer c) throws SQLException {
        String sql = "INSERT INTO customer (NAME, EMAIL) VALUES (?, ?)";
        String [] cols = {"CUSTOMER_ID"};
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql, cols)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setCustomerId(rs.getLong(1));
                }
            }
        }
        return c;
    }

    public Customer findCustomerById(Long id) throws SQLException {
        String sql = "SELECT CUSTOMER_ID, NAME, EMAIL FROM CUSTOMER WHERE CUSTOMER_ID = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                   return new Customer (rs.getLong("CUSTOMER_ID"), rs.getString("NAME"), rs.getString("EMAIL"));
                }
            }
        }
        return null;
    }

    public List<Customer> listCustomers() throws SQLException {
        List<Customer> out = new ArrayList<>();
        String sql = "SELECT CUSTOMER_ID, NAME, EMAIL FROM CUSTOMER ORDER BY CUSTOMER_ID";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Customer(rs.getLong("CUSTOMER_ID"), rs.getString("NAME"), rs.getString("EMAIL")));
            }
        }
        return out;
    }

    // Gift CRUD
    public Gift createGift(Gift g) throws SQLException {
        String sql = "INSERT INTO GIFT(NAME, PRICE, IN_STOCK) VALUES(?, ?, ?)";
        String[] cols = {"GIFT_ID"};
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, cols)) {
            ps.setString(1, g.getName());
            ps.setDouble(2, g.getPrice());
            ps.setInt(3, g.getInStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) g.setGiftId(rs.getLong(1));
            }
        }
        return g;
    }

    public Gift findGiftById(Long id) throws SQLException {
        String sql = "SELECT GIFT_ID, NAME, PRICE, IN_STOCK FROM GIFT WHERE GIFT_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Gift(rs.getLong("GIFT_ID"), rs.getString("NAME"), rs.getDouble("PRICE"), rs.getInt("IN_STOCK"));
            }
        }
        return null;
    }

    public List<Gift> listGifts() throws SQLException {
        List<Gift> out = new ArrayList<>();
        String sql = "SELECT GIFT_ID, NAME, PRICE, IN_STOCK FROM GIFT ORDER BY GIFT_ID";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Gift(rs.getLong("GIFT_ID"), rs.getString("NAME"), rs.getDouble("PRICE"), rs.getInt("IN_STOCK")));
        }
        return out;
    }

    // Orders
    public OrderDTO createOrder(OrderDTO order) throws SQLException {
        // Reduce stock and insert order in a transaction
        String updateStock = "UPDATE GIFT SET IN_STOCK = IN_STOCK - ? WHERE GIFT_ID = ? AND IN_STOCK >= ?";
        String insertOrder = "INSERT INTO ORDERS(CUSTOMER_ID, GIFT_ID, QUANTITY, TOTAL_PRICE) VALUES(?, ?, ?, ?)";
        String[] cols = {"ORDER_ID"};

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pst = conn.prepareStatement(updateStock)) {
                pst.setInt(1, order.getQuantity());
                pst.setLong(2, order.getGiftId());
                pst.setInt(3, order.getQuantity());
                int rows = pst.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    throw new SQLException("Stock insuficiente o regalo no existe");
                }
            }
            try (PreparedStatement psi = conn.prepareStatement(insertOrder, cols)) {
                psi.setLong(1, order.getCustomerId());
                psi.setLong(2, order.getGiftId());
                psi.setInt(3, order.getQuantity());
                psi.setDouble(4, order.getTotalPrice());
                psi.executeUpdate();
                try (ResultSet rs = psi.getGeneratedKeys()) {
                    if (rs.next()) order.setOrderId(rs.getLong(1));
                }
            }
            conn.commit();
        }
        return order;
    }

    public List<OrderDTO> listOrders() throws SQLException {
        List<OrderDTO> out = new ArrayList<>();
        String sql = "SELECT ORDER_ID, CUSTOMER_ID, GIFT_ID, QUANTITY, TOTAL_PRICE FROM ORDERS ORDER BY ORDER_ID";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new OrderDTO(rs.getLong("ORDER_ID"), rs.getLong("CUSTOMER_ID"), rs.getLong("GIFT_ID"), rs.getInt("QUANTITY"), rs.getDouble("TOTAL_PRICE")));
            }
        }
        return out;
    }
}