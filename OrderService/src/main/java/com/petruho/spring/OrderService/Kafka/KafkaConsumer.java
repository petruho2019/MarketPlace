package com.petruho.spring.OrderService.Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    public KafkaConsumer(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics ="topic_order", topicPartitions = @TopicPartition(topic = "topic_order", partitions = {"0"}))
    public void listenOrder(ConsumerRecord<String, String> record) throws InterruptedException {
        System.out.println(record.value());
        System.out.println(record.value());

        kafkaProducer.sendStatusOrder("Order is being processed");

        Thread.sleep(10000);

        kafkaProducer.sendStatusOrder("Order complete");
    }
}
