package com.nemchann.id_generation.service;

import org.springframework.stereotype.Service;

@Service
public class SnowflakeService {
    // Кастомная эпоха (точка отсчета). Например, 1 января 2024 года в миллисекундах.
    // Мы вычитаем это из текущего времени, чтобы сэкономить биты.
    private static final long CUSTOM_EPOCH = 1704067200000L;

    // Количество бит под каждую часть
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    // Максимальные значения (31 для ЦОД и Worker, 4095 для Sequence)
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // Сдвиги влево для склеивания числа
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    // Состояние генератора
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    // Метод генерации (synchronized, чтобы в многопоточности не было сбоев)
    public synchronized long generateId(long datacenterId, long workerId) {
        // Проверка параметров, чтобы они не превышали 31 (5 бит)
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID должен быть от 0 до 31");
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID должен быть от 0 до 31");
        }

        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Часы пошли назад! Отказ в генерации ID.");
        }

        // Если генерируем несколько ID в одну и ту же миллисекунду
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // Если превысили 4096 в миллисекунду, ждем следующую миллисекунду
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // Если миллисекунда поменялась, сбрасываем счетчик
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // "Склеиваем" все части с помощью побитового ИЛИ (|) и сдвигов (<<)
        return ((timestamp - CUSTOM_EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
