package com.lostfound.MatchingService.service;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class RedisHealthService {

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisHealthService(
            RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public String checkRedisConnection() {

        try (RedisConnection connection =
                     redisConnectionFactory.getConnection()) {

            String response = connection.ping();

            if ("PONG".equalsIgnoreCase(response)) {
                return "Redis connection successful";
            }

            return "Redis connection failed";

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Unable to connect to Redis",
                    exception
            );
        }
    }
}
