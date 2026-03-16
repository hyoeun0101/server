package kr.hhplus.be.server.concert.usecase;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.concert.domain.model.Seat;
import kr.hhplus.be.server.concert.port.in.PointUseCase;
import kr.hhplus.be.server.concert.port.out.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PointService implements PointUseCase {
    private final SeatPort seatPort;
    private final QueueTokenPort queueTokenPort;
    private final UserPointPort userPointPort;
    private final PaymentPort paymentPort;
    private final TimeProvider timeProvider;

    public PointService(SeatPort seatPort, QueueTokenPort queueTokenPort, UserPointPort userPointPort, PaymentPort paymentPort, TimeProvider timeProvider) {
        this.seatPort = seatPort;
        this.queueTokenPort = queueTokenPort;
        this.userPointPort = userPointPort;
        this.paymentPort = paymentPort;
        this.timeProvider = timeProvider;
    }

    @Override
    @Transactional
    public PayResult pay(PayCommand command) {
        Instant now = timeProvider.now();
        //1) 토큰 ACTIVE 검증
        QueueTokenPort.ActiveToken token = queueTokenPort.requireActiveForUpdate(command.quequeTokenId());
        String userUuid = token.userUuid();

        //2) 좌석 조회
        Seat seat = seatPort.findById(command.seatId()).orElseThrow(() -> new IllegalArgumentException("seat not found"));

        //3) 결제 가능 조건 확인
        if (seat.getStatus() != Seat.Status.HELD) throw new IllegalArgumentException("seat not held");
        if (seat.isHoldExpired(now)) {
            seat.releaseHold();
            seatPort.save(seat);
            throw new IllegalArgumentException("hold expired");
        }

        if (!userUuid.equals(seat.getHeldByUserUusid())) throw new SecurityException("not seat holder");

        // 4) 포인트 차감
        UserPointPort.UserPointAccount account = userPointPort.loadForUpdate(userUuid);
        UserPointPort.UserPointAccount newAccount = account.spend(command.amount());

        userPointPort.save(newAccount);

        //5) 좌석 확정 + 결제 생성 + 토큰 만료
        seat.confirm(userUuid);
        seatPort.save(seat);

        long paymentId = paymentPort.createPaymentGetPaymentId(userUuid, command.seatId(), command.amount(), now);
        queueTokenPort.expireToken(command.quequeTokenId());

        return new PayResult(paymentId, seat.getSeatId(), now);
    }

    @Override
    @Transactional
    public ChargeResult chargePoint(ChargeCommand command) {
        queueTokenPort.requireActiveForUpdate(command.tokenId());

        UserPointPort.UserPointAccount account = userPointPort.loadForUpdate(command.userUuid());
        UserPointPort.UserPointAccount updated = account.charge(command.amount());

        userPointPort.save(updated);

        return new ChargeResult(updated.userUuid(), updated.balance());
    }

    @Override
    @Transactional
    public GetResult getPoint(GetCommand command) {
        queueTokenPort.requireActiveForUpdate(command.tokenId());

        UserPointPort.UserPointAccount account = userPointPort.loadForUpdate(command.userUuid());

        return new GetResult(command.userUuid(), account.balance());
    }
}
