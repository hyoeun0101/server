package kr.hhplus.be.server.concert.port.out;

import java.time.Instant;

public interface PaymentPort {
    long createPaymentGetPaymentId(String userUuid, long seatId, long amount, Instant paidAt);
}
