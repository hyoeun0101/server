package kr.hhplus.be.server.concert.port.out;

import java.time.Duration;

public interface QueuePolicyPort {
    int maxActiveUsers();
    Duration activeTtl();
}
