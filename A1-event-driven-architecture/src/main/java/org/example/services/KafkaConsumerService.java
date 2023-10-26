package org.example.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "testTopic", groupId = "group_id")
    public void listen(String message) {
        System.out.println("Message received: " + message);
    }
}
