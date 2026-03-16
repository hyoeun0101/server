package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from QueueTokenEntity  t where t.tokenId = :tokenId")
    Optional<QueueTokenEntity> lockByTokenId(@Param("tokenId") String tokenId);

    @Query("""
    select count(t)
    from QueueTokenEntity t
    where t.status = 'WAITING'
    and t.createdAt < :createdAt
    """)
    int countWaitingBefore(@Param("createdAt")Instant createdAt);

    long countByStatus(String status);
}
