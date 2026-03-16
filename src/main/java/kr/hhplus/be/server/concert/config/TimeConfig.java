package kr.hhplus.be.server.concert.config;

import kr.hhplus.be.server.concert.port.out.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class TimeConfig {
    @Bean
    public TimeProvider timeProvider() {
        return Instant::now;
    }

}
