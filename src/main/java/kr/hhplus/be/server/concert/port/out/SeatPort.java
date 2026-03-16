package kr.hhplus.be.server.concert.port.out;

import kr.hhplus.be.server.concert.domain.model.Seat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SeatPort {
    Optional<Seat> findById(long seatId);

    Optional<Seat> loadForUpdate(LocalDate date, int seatNo);

    Seat save(Seat seat);

    List<Seat> findByDate(LocalDate date);
    List<LocalDate> findAvailableDates();
}
