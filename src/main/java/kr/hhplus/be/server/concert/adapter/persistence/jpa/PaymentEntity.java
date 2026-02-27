package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "user_uuid", nullable = false, length = 36)
    public String userUuid;

    @Column(name = "seat_id", nullable = false)
    public Long seatId;

    @Column(nullable = false)
    public long amount;

    @Column(name = "paid_at", nullable = false)
    public Instant paidAt;
}
