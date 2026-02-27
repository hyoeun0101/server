package kr.hhplus.be.server.concert.port.in;

import java.time.Instant;

public interface ExpireHoldUseCase {
    ExpireResult expireNow();

    record ExpireResult(int releasedCount, Instant ranAt) {}
}
