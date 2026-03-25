package com.nemchann.id_generation.controller;

import com.nemchann.id_generation.service.SnowflakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/id")
@Tag(name = "Генератор ID", description = "Сервис для распределенной генерации уникальных идентификаторов")
public class IdGeneratorController {
    private final SnowflakeService snowflakeService;

    public IdGeneratorController(SnowflakeService snowflakeService) {
        this.snowflakeService = snowflakeService;
    }

    @GetMapping("/uuid")
    @Operation(summary = "Сгенерировать UUID", description = "Возвращает 36-символьную строку (например: 550e8400-e29b-41d4-a716-446655440000)")
    public String getUuid() {
        return UUID.randomUUID().toString();
    }

    @GetMapping("/snowflake")
    @Operation(summary = "Сгенерировать Twitter Snowflake ID", description = "Возвращает 64-битное уникальное число (long)")
    public long getSnowflakeId(
            @Parameter(description = "ID дата-центра (от 0 до 31)", example = "1")
            @RequestParam long datacenterId,

            @Parameter(description = "ID компьютера/воркера (от 0 до 31)", example = "1")
            @RequestParam long workerId) {

        return snowflakeService.generateId(datacenterId, workerId);
    }
}
