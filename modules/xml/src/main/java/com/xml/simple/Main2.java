package com.xml.simple;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import com.model.Address;
import com.model.Customer;
import com.model.Item;
import com.model.Order;

import java.io.File;
import java.util.Date;

public class Main2 {

    public static void main(String[] args) throws Exception {
        Customer customer = new Customer(111L, "张三");
        Order order1 = new Order(1L, new Date());
        Order order2 = new Order(2L, new Date());
        Item item11 = new Item(1, 11L);
        Item item21 = new Item(2, 21L);
        Address address = new Address("450000", "瑞达路XX#", true);

        customer.setAddress(address);
        customer.getOrderList().add(order1);
        customer.getOrderList().add(order2);
        order1.getItemList().add(item11);
        order2.getItemList().add(item21);


        Serializer serializer = new Persister();
        File result = new File("E:/tmp/customer.xml");

        serializer.write(customer, result);

        Customer _obj = serializer.read(Customer.class, result);
        System.out.println(_obj.getName());
        System.out.println(_obj.getOrderList().get(0).getCdate());

    }
}