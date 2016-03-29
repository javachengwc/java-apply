package com.model;

import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.List;

@Root
public class Customer {
    @Attribute(required = true)
    private Long id;
    @Element(required = true)
    private String name;
    @Element(required = false)
    private Address address;
    @ElementList(required = false, type = Order.class)
    private List<Order> orderList = new ArrayList<Order>(0);

    public Customer() {
    }

    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
