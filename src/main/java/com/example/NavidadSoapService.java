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
}