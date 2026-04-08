package com.nemchann.bookingservice.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue requestQueue() {
        return new Queue("payment.request", false);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue("payment.response", false);
    }

    // Это важно для автоматической конвертации объектов в JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
