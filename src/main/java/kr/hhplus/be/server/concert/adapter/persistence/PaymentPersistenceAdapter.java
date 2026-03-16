package kr.hhplus.be.server.concert.adapter.persistence;

import kr.hhplus.be.server.concert.adapter.persistence.jpa.PaymentEntity;
import kr.hhplus.be.server.concert.adapter.persistence.jpa.PaymentJpaRepository;
import kr.hhplus.be.server.concert.port.out.PaymentPort;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentPersistenceAdapter implements PaymentPort {
    private final PaymentJpaRepository repo;

    public PaymentPersistenceAdapter(PaymentJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public long createPaymentGetPaymentId(String userUuid, long seatId, long amount, Instant paidAt) {
        PaymentEntity e = new PaymentEntity();
        e.userUuid = userUuid;
        e.seatId = seatId;
        e.amount = amount;
        e.paidAt = paidAt;
        return repo.save(e).id;
    }
}
