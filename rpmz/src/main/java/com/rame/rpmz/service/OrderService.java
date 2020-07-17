package com.rame.rpmz.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.rame.rpmz.dto.Order;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private List<Order> orders = new ArrayList<Order>();

    @PostConstruct
    public void initDB() {
        orders.add(new Order(1, "Book", 200.0));
        orders.add(new Order(2, "Mobile", 20000.0));
        orders.add(new Order(3, "Charger", 500.0));
        orders.add(new Order(4, "hard Disk", 5000.0));
    }

    public Order addOrder(Order o) {
        orders.add(o);
        return o;
    }

    public List<Order> getOrders() {
       return this.orders;
        
    }
}