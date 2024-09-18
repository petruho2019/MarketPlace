package com.petruho.spring.project.marketplace.kafka;


import com.petruho.spring.project.marketplace.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrder(Order order) {
        kafkaTemplate.send("topic_order",0,"order",order.toString());
        System.out.println("Order sent to topic_order: " + order);
    }
}
