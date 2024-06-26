package org.example.web;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.example.entities.PageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class PageEventRestController {

    private InteractiveQueryService interactiveQueryService;
    private StreamBridge streamBridge;

    @GetMapping("/publish/{topic}/{name}")
    public PageEvent publish(@PathVariable String topic, @PathVariable String name) {
        PageEvent pageEvent = new PageEvent(name, Math.random() > 0.5 ? "U1" : "U2", new Date(), new Random().nextLong(5000));
        pageEvent.setUser(Math.random() > 0.5 ? "U1" : "U2");
        streamBridge.send(topic, pageEvent);
        return pageEvent;
    }

    @GetMapping(value = "/analytics", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Long>> analytics() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> {
                    Map<String, Long> map = new HashMap<>();
                    ReadOnlyKeyValueStore<String, Long> stats = interactiveQueryService.getQueryableStore("count-store", QueryableStoreTypes.keyValueStore());
                    Instant now = Instant.now();
                    Instant from = now.minusSeconds(5);
                    KeyValueIterator<String, Long> keyValueIterator = stats.all();
                    while (keyValueIterator.hasNext()) {
                        KeyValue<String, Long> next = keyValueIterator.next();
                        map.put(next.key, next.value);
                    }
                    return map;
                });
    }

    @GetMapping(value = "/analyticsAggregate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Double>> analyticsAggregate() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> {
                    Map<String, Double> map = new HashMap<>();
                    ReadOnlyWindowStore<String, Double> stats = interactiveQueryService.getQueryableStore("total-store", QueryableStoreTypes.windowStore());
                    Instant now = Instant.now();
                    Instant from = now.minusSeconds(30);
                    KeyValueIterator<Windowed<String>, Double> windowedLongKeyValueIterator = stats.fetchAll(from, now);
                    while (windowedLongKeyValueIterator.hasNext()) {
                        KeyValue<Windowed<String>, Double> next = windowedLongKeyValueIterator.next();
                        map.put(next.key.key(), next.value);
                    }
                    return map;
                });
    }
}
