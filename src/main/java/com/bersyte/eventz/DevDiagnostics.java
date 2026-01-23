package com.bersyte.eventz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDiagnostics {
    
    @Bean
    public CommandLineRunner run(){
        return args -> System.out.println("Hello from dev env");
    }
}
