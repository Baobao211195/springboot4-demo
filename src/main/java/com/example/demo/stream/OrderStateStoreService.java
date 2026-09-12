package com.example.demo.stream;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class OrderStateStoreService {

    private static final String STORE_NAME = "order-history-store";

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public OrderStateStoreService(
            StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    public OrderHistory get(Integer orderId) {

        KafkaStreams kafkaStreams =
                streamsBuilderFactoryBean.getKafkaStreams();

        if (kafkaStreams == null) {
            throw new IllegalStateException(
                    "Kafka Streams is not initialized yet");
        }

        ReadOnlyKeyValueStore<Integer, OrderHistory> store =
                kafkaStreams.store(
                        StoreQueryParameters.fromNameAndType(
                                STORE_NAME,
                                QueryableStoreTypes.keyValueStore()
                        )
                );

        return store.get(orderId);
    }
}