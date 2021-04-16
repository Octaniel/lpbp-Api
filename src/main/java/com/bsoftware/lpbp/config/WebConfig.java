package com.bsoftware.lpbp.config;

import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableCaching
public class WebConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(TaskSchedulerBuilder builder) {
        return builder.build();
    }
}
