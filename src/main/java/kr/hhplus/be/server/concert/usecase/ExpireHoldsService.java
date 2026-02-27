package kr.hhplus.be.server.concert.usecase;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.concert.port.in.ExpireHoldUseCase;
import kr.hhplus.be.server.concert.port.out.HoldExpiryPort;
import kr.hhplus.be.server.concert.port.out.TimeProvider;

import java.time.Instant;


public class ExpireHoldsService implements ExpireHoldUseCase {
    private final HoldExpiryPort holdExpiryPort;

    private final TimeProvider timeProvider;

    public ExpireHoldsService(HoldExpiryPort holdExpiryPort, TimeProvider timeProvider) {
        this.holdExpiryPort = holdExpiryPort;
        this.timeProvider = timeProvider;
    }

    @Override
    @Transactional
    public ExpireResult expireNow() {
        Instant now = timeProvider.now();
        int released = holdExpiryPort.releaseExpiredHolds(now);
        return new ExpireResult(released, now);
    }
}
