package kr.hhplus.be.server.concert.adapter.persistence;

import kr.hhplus.be.server.concert.adapter.persistence.jpa.SeatEntity;
import kr.hhplus.be.server.concert.adapter.persistence.jpa.SeatJpaRepository;
import kr.hhplus.be.server.concert.domain.model.Seat;
import kr.hhplus.be.server.concert.port.out.SeatPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class SeatPersistenceAdapter implements SeatPort {
    private final SeatJpaRepository repository;

    public SeatPersistenceAdapter(SeatJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Seat> findById(long seatId) {
        return repository.findById(seatId).map(this::toDomain);
    }

    @Override
    public Optional<Seat> loadForUpdate(LocalDate date, int seatNo) {
        return repository.lockByDateAndSeatNo(date, seatNo).map(this::toDomain);
    }

    @Override
    public Seat save(Seat seat) {
        SeatEntity e = repository.findById(seat.getSeatId()).orElseGet(SeatEntity::new);
        this.apply(e, seat);
        SeatEntity saved = repository.save(e);
        return toDomain(saved);
    }

    @Override
    public List<Seat> findByDate(LocalDate date) {
        return repository.findAllByDate(date).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<LocalDate> findAvailableDates() {
        return repository.findAvailableDates();
    }

    private Seat toDomain(SeatEntity entity) {
        return new Seat(
                entity.id,
                entity.concertDate,
                entity.seatNo,
                Seat.Status.valueOf(entity.status),
                entity.heldByUserUuid,
                entity.holdExpiresAt,
                entity.confirmedByUserUuid
        );
    }

    private void apply(SeatEntity e, Seat seat) {
        e.id = seat.getSeatId();
        e.concertDate = seat.getDate();
        e.seatNo = seat.getSeatNo();
        e.status = seat.getStatus().name();
        e.heldByUserUuid = seat.getHeldByUserUusid();
        e.holdExpiresAt = seat.getHoldExpriesAt();
    }
}
