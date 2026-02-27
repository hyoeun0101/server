package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
