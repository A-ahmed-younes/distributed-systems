package org.example.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka-supplier")
public class KafkaSupplierService {
    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public KafkaSupplierService(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/publish/{message}")
    public String sendMessageToKafkaTopic(@PathVariable String message) {
        kafkaTemplate.send("supplierTopic", 1, message);
        return "Message sent successfully!";
    }
}
