package com.nemchann.producer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // очередь "notification.queue"
    @Bean
    public Queue notificationQueue() {
        return new Queue("notification.queue", true);
    }
}