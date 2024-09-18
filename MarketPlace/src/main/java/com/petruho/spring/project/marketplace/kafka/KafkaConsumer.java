package com.petruho.spring.project.marketplace.kafka;


import com.petruho.spring.project.marketplace.controller.OrderController;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrderController orderController ;

    @KafkaListener(topics = "topic_order", topicPartitions = @TopicPartition(topic = "topic_order", partitions = {"1"}))
    public void listenStatusOrder(ConsumerRecord<String, String> record) {

        orderController.changeOrderStatus(record.value());
    }
}
