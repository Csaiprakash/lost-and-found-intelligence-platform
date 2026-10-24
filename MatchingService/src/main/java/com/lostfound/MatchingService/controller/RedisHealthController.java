package com.lostfound.MatchingService.controller;

import com.lostfound.MatchingService.service.RedisHealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/redis")
public class RedisHealthController {

    private final RedisHealthService redisHealthService;

    public RedisHealthController(
            RedisHealthService redisHealthService) {
        this.redisHealthService = redisHealthService;
    }

    @GetMapping("/health")
    public String checkRedisHealth() {
        return redisHealthService.checkRedisConnection();
    }
}
