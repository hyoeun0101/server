package kr.hhplus.be.server.concert.adapter.scheduler;

import kr.hhplus.be.server.concert.port.in.ExpireHoldUseCase;
import kr.hhplus.be.server.concert.port.in.HoldSeatUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HoldExpiryScheduler {
    private final ExpireHoldUseCase expireHoldUseCase;

    public HoldExpiryScheduler(ExpireHoldUseCase expireHoldUseCase) {
        this.expireHoldUseCase = expireHoldUseCase;
    }


    @Scheduled(fixedDelayString = "${seat.hold-expiry-scan-ms:30000}")
    public void run() {
        expireHoldUseCase.expireNow();
    }
}
