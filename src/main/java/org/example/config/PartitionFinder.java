package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
public class PartitionFinder {
    private final ConsumerFactory<String, Object> consumerFactory;

    public PartitionFinder(ConsumerFactory<String, Object> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    public String[] partitions(String topic) {
        try (Consumer<String, Object> consumer = consumerFactory.createConsumer()) {
            var partitions = getPartitions(consumer, topic);
            String[] partitionIds = partitions.stream()
                    .map(pi -> "" + pi.partition())
                    .toArray(String[]::new);
            log.debug("Assigned partitions: {}", StringUtils.arrayToDelimitedString(partitionIds, ","));
            return partitionIds;
        }
    }

    private List<TopicPartition> getPartitions(Consumer<String, Object> consumer, String topicName) {
        return consumer.partitionsFor(topicName)
                .stream()
                .map(it -> new TopicPartition(it.topic(), it.partition()))
                .toList();
    }
}
