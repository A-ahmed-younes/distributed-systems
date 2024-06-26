package org.example;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.example.entities.PageEvent;
import org.example.services.AppSerdesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class EventDrivenArchitectureDemo {
    public static void main(String[] args) {
        SpringApplication.run(EventDrivenArchitectureDemo.class, args);
    }

    CommandLineRunner start() {
        return args -> {
            System.out.println("*********************************");
            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            StreamsBuilder builder = new StreamsBuilder();
            final Serde<String> stringSerde = Serdes.String();
            final Serde<Long> longSerde = Serdes.Long();
            KStream<String, PageEvent> input = builder.stream("R333", Consumed.with(stringSerde, AppSerdesService.PageEventSerdes()));
            KStream<String, Long> resultStream = input
                    .filter((k, v) -> v.getDuration() > 100)
                    .map((k, v) -> new KeyValue<>(v.getName(), v.getDuration()))
                    .map((k, v) -> new KeyValue<>(k + "->", v))
                    .peek((k, v) -> System.out.println(k + "=>" + v));
            resultStream.to("R66", Produced.with(Serdes.String(), Serdes.Long()));
            Topology topology = builder.build();
            KafkaStreams kafkaStreams = new KafkaStreams(topology, props);
            kafkaStreams.start();

        };
    }
}
