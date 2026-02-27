package kr.hhplus.be.server.concert.port.in;

import java.time.Instant;

public interface PointUseCase {
    record PayCommand(String quequeTokenId, long seatId, long amount) {}
    record PayResult(long paymentId, long seatId, Instant paidAt) {}

    PayResult pay(PayCommand command);

    record ChargeCommand(String tokenId, String userUuid, long amount){}
    record ChargeResult(String userUuid, long balance) {}

    ChargeResult chargePoint(ChargeCommand command);

    record GetCommand(String tokenId, String userUuid) {}
    record GetResult(String userUuid, long balance) {}

    GetResult getPoint(GetCommand command);
}
