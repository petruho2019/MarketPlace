package com.petruho.spring.project.marketplace.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Setter
@Getter
public class Order {

    private Map<Item,Long> order = new HashMap<>();
    private long quantity;
    private String orderStatus;

    public Map<Item,Long> getAllItemInOrder(){
        return order;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order=" + order +
                ", quantity=" + quantity +
                '}';
    }
}
