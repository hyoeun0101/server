package kr.hhplus.be.server.concert.usecase;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.concert.domain.model.Seat;
import kr.hhplus.be.server.concert.port.in.HoldSeatUseCase;
import kr.hhplus.be.server.concert.port.out.HoldPolicyPort;
import kr.hhplus.be.server.concert.port.out.QueueTokenPort;
import kr.hhplus.be.server.concert.port.out.SeatPort;
import kr.hhplus.be.server.concert.port.out.TimeProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HoldSeatService implements HoldSeatUseCase {
    private final SeatPort seatPort;
    private final QueueTokenPort queueTokenPort;
    private final HoldPolicyPort holdPolicyPort;
    private final TimeProvider timeProvider;

    public HoldSeatService(SeatPort seatPort, QueueTokenPort queueTokenPort, HoldPolicyPort holdPolicyPort, TimeProvider timeProvider) {
        this.seatPort = seatPort;
        this.queueTokenPort = queueTokenPort;
        this.holdPolicyPort = holdPolicyPort;
        this.timeProvider = timeProvider;
    }

    @Override
    @Transactional
    public HoldSeatResult hold(HoldSeatCommand command) {
        Instant now = timeProvider.now();

        // 1) 대기열 ACTIVE 검증
        QueueTokenPort.ActiveToken token = queueTokenPort.requireActiveForUpdate(command.quequeTokenId());
        String userUuid = token.userUuid();

        // 2) 좌석 조회 및 업데이트
        Seat seat = seatPort.loadForUpdate(command.date(), command.seatNo())
                .orElseThrow(() -> new IllegalArgumentException("seat not found"));


        // 3) 좌석 만료 상태면 hold 해제
        if (seat.isHoldExpired(now)) seat.releaseHold();

        // 4) 좌석 상태 확인
        if (seat.getStatus() != Seat.Status.AVAILABLE) {
            throw new IllegalStateException("seat is not available");
        }
        // 5) 좌석 hold 처리
        seat.hold(userUuid, now.plus(holdPolicyPort.holdDuration()));
        seatPort.save(seat);

        return new HoldSeatResult(
                seat.getSeatId(),
                seat.getStatus().name(),
                seat.getHeldByUserUusid(),
                seat.getHoldExpriesAt().toString()
        );
    }
}
