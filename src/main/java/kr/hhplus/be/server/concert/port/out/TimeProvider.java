package kr.hhplus.be.server.concert.port.out;

import java.time.Instant;

@FunctionalInterface
public interface TimeProvider {
    Instant now();
}
