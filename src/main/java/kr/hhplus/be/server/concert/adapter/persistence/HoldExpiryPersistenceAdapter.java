package kr.hhplus.be.server.concert.adapter.persistence;

import kr.hhplus.be.server.concert.adapter.persistence.jpa.SeatJpaRepository;
import kr.hhplus.be.server.concert.port.out.HoldExpiryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class HoldExpiryPersistenceAdapter implements HoldExpiryPort {

    private final SeatJpaRepository seatJpaRepository;

    public HoldExpiryPersistenceAdapter(SeatJpaRepository seatJpaRepository) {
        this.seatJpaRepository = seatJpaRepository;
    }

    @Override
    public int releaseExpiredHolds(Instant now) {
        return seatJpaRepository.releaseExpiredHolds(now);
    }
}
