package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;


@Configuration
public class CommonKafkaConfig {

    @Bean
    public KafkaListenerContainerFactory manualListenerContainerFactory(ConsumerFactory consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        //use default factory
        factory.setConsumerFactory(consumerFactory);
        var props = factory.getContainerProperties();
        props.setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public PartitionFinder сommonPartitionFinder(ConsumerFactory<String, Object> consumerFactory) {
        return new PartitionFinder(consumerFactory);
    }
}
