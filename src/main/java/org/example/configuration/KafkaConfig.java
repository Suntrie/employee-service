package org.example.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic replyProjectTopic(
            @Value("${spring.kafka.topic.on-employee-change}") String topicName
    ) {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .build();
    }
}
