package com.bersyte.eventz.features.events.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeConfig {
    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    }
}
