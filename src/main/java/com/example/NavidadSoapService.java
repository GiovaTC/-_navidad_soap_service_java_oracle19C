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