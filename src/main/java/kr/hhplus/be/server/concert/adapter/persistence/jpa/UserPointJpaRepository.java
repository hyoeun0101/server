package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from UserPointEntity p where p.userUuid = :userUuid")
    Optional<UserPointEntity> lockByUserUuid(@Param("userUuid") String userUuid);
}
