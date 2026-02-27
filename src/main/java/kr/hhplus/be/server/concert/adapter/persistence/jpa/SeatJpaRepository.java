package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SeatEntity s where s.concertDate = :date and s.seatNo = :seatNo")
    Optional<SeatEntity> lockByDateAndSeatNo(@Param("date")LocalDate date, @Param("seatNo") int seatNo);

    @Query("""
        select s
        from SeatEntity s
        where s.concertDate = :date
        order by s.seatNo asc
        """)
    List<SeatEntity> findAllByDate(@Param("date") LocalDate date);

    @Query("""
        select distinct s.concertDate
        from SeatEntity s
        where s.status = 'AVAILABLE'
        order by s.concertDate asc
    """)
    List<LocalDate> findAvailableDates();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
    update SeatEntity s
    set s.status = 'AVAILABLE',
        s.heldByUserUuid = null,
        s.holdExpiresAt = null
    where s.status = 'HOLD'
      and s.holdExpiresAt is not null
      and s.holdExpiresAt < :now 
""")
    int releaseExpiredHolds(@Param("now")Instant now);
}
