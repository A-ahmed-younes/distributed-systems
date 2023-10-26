package org.example.services;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.example.entities.PageEvent;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class AppSerdesService {
    public static Serde<PageEvent> PageEventSerdes() {
        JsonSerializer<PageEvent> pageEventJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<PageEvent> pageEventJsonDeserializer = new JsonDeserializer<>(PageEvent.class);
        return Serdes.serdeFrom(pageEventJsonSerializer, pageEventJsonDeserializer);

    }
}
