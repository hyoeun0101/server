package kr.hhplus.be.server.concert.port.in;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface SeatQueryUseCase {
    GetAvailableDateResult getAvailableDates();
    GetSeatsResult getSeats(LocalDate date);

    record GetAvailableDateResult(List<LocalDate> dates){}

    record SeatView(long seatId,
                  LocalDate date,
                  int seatNo,
                  String status,
                  Instant holdExpiresAt) {}

    record GetSeatsResult(List<SeatView> seats){}
}
