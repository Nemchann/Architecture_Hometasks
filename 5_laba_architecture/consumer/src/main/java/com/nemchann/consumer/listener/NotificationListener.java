package com.nemchann.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    // Создаем логгер
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @RabbitListener(queuesToDeclare = @Queue("${rabbitmq.queue.notification}"))
    public void receiveMessage(String message) {
        // Выводим
        log.info("🔔 Получено уведомление из RabbitMQ: {}", message);
    }
}
