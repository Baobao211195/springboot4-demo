package com.example.demo.stream;

import com.example.demo.cdc.OrderCdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@Configuration
@EnableKafkaStreams
@Slf4j
public class StreamConfig {

    private static final String ORDER_CDC_TOPIC = "mysql.mydatabase.orders";
    private static final String ORDER_HISTORY_TOPIC = "order-hist";
    private static final String ORDER_HISTORY_STORE =  "order-history-store";

    @Bean
    public KTable<Integer, OrderHistory> orderTable(
            StreamsBuilder builder,
            Serde<OrderCdcEvent> orderCdcEventSerde,
            Serde<OrderHistory> orderHistorySerde) {

        KStream<String, OrderCdcEvent> stream =
                builder.stream(
                        ORDER_CDC_TOPIC,
                        Consumed.with(
                                Serdes.String(),
                                orderCdcEventSerde
                        )
                );

        return stream
                .filter((key, event) ->
                        event != null
                                && event.payload() != null
                                && event.payload().after() != null
                                && ("c".equals(event.payload().op())
                                || "u".equals(event.payload().op()))
                )

                // order.id becomes the KTable key
                .selectKey((key, event) ->
                        Math.toIntExact(
                                event.payload().after().id()
                        )
                )

                // OrderCdcEvent -> OrderHistory
                .mapValues(event -> {
                    var order = event.payload().after();

                    return new OrderHistory(
                            Math.toIntExact(order.id()),
                            order.name(),
                            order.address(),
                            order.age()
                    );
                })

                // Group by orderId
                .groupByKey(
                    Grouped.with(
                        Serdes.Integer(),
                        orderHistorySerde
                    )
                )

                // Keep latest OrderHistory for each orderId
                .reduce(
                    (oldValue, newValue) -> {
                        log.info("newValue: {}", newValue);
                       return newValue;
                    },

                    // Materialize KTable into named state store
                    Materialized
                        .<Integer, OrderHistory, KeyValueStore<Bytes, byte[]>>as(ORDER_HISTORY_STORE)
                        .withKeySerde(Serdes.Integer())
                        .withValueSerde(orderHistorySerde)
                );
    }

    @Bean
    public KStream<String, OrderCdcEvent> orderCdcStream(
            StreamsBuilder builder,
            Serde<OrderCdcEvent> orderCdcEventSerde,
            Serde<OrderHistory> orderHistorySerde) {

        // 1. Read CDC event from Debezium topic
        KStream<String, OrderCdcEvent> orderCdcStream =
                builder.stream(
                        ORDER_CDC_TOPIC,
                        Consumed.with(
                                Serdes.String(),
                                orderCdcEventSerde
                        )
                );

        // 2. Transform OrderCdcEvent -> OrderHistory
        KStream<String, OrderHistory> orderHistoryStream =
                orderCdcStream
                        .filter((key, event) ->
                                event != null
                                        && event.payload() != null
                                        && event.payload().after() != null
                                        && ("c".equals(event.payload().op())
                                        || "u".equals(event.payload().op()))
                        )
                        .peek((_, event) ->
                                log.info("CDC event - {}", event)
                        )
                        .mapValues(event -> {
                            var order = event.payload().after();
                            return new OrderHistory(
                                Math.toIntExact(order.id()),
                                order.name(),
                                order.address(),
                                order.age()
                            );

                        })
                        .peek((_, event) ->
                                log.info("History event - {}", event)
                        );

        // 3. Publish transformed event to order-hist
        orderHistoryStream.to(
                ORDER_HISTORY_TOPIC,
                Produced.with(
                        Serdes.String(),
                        orderHistorySerde
                )
        );

        return orderCdcStream;
    }

    /**
     * Serde for Debezium CDC event.
     */
    @Bean
    public Serde<OrderCdcEvent> orderCdcEventSerde() {
        return Serdes.serdeFrom(
                new JacksonJsonSerializer<OrderCdcEvent>(),
                new JacksonJsonDeserializer<>(
                        OrderCdcEvent.class
                )
        );
    }

    /**
     * Serde for transformed OrderHistory event.
     */
    @Bean
    public Serde<OrderHistory> orderHistorySerde() {
        return Serdes.serdeFrom(
                new JacksonJsonSerializer<OrderHistory>(),
                new JacksonJsonDeserializer<>(
                        OrderHistory.class
                )
        );
    }
}