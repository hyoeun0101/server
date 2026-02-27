package kr.hhplus.be.server.concert.config;

import kr.hhplus.be.server.concert.domain.model.HoldPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class PolicyConfig {
    @Bean
    public HoldPolicy holdPolicy() {
        return new HoldPolicy(Duration.ofMinutes(5));
    }
}
