package com.nemchann.notificationservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @RabbitListener(queues = "notification.request")
    public void handleNotification(String message) {
        System.out.println("🔔 [NOTIFICATION SERVICE] Получено новое уведомление:");
        System.out.println("📢 Сообщение для клиента: " + message);
        System.out.println("✅ Уведомление успешно отправлено (PUSH/Email/SMS)");
        System.out.println("------------------------------------------------");
    }
}