package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@KafkaListener(topicPartitions = @org.springframework.kafka.annotation.TopicPartition(topic = "${kafka-topics.my-event-topic}",
        partitions = "#{@сommonPartitionFinder.partitions('${kafka-topics.my-event-topic}')}"),
        containerFactory = "manualListenerContainerFactory",
        properties = {"enable.auto.commit:false", "auto.offset.reset:latest"},
        idIsGroup = false,
        autoStartup = "true")
public class MyEventListener implements ConsumerSeekAware {


    @KafkaHandler
    public void receiverHandle(@Payload(required = false) Object message,
                               Acknowledgment acknowledgment) {
        try {
            log.info("Hello message {}", message);
        } catch (Exception e) {
            log.error("error when process event", e);
        } finally {
            log.info("Done");
        }
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekAware.ConsumerSeekCallback callback) {
        log.info("assign partitions with offset {}", assignments);
        ConsumerSeekAware.super.onPartitionsAssigned(assignments, callback);
    }
}
