package kr.hhplus.be.server.concert.usecase;

import kr.hhplus.be.server.concert.port.in.SeatQueryUseCase;
import kr.hhplus.be.server.concert.port.out.SeatPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeatQueryService implements SeatQueryUseCase {

    private final SeatPort seatPort;

    public SeatQueryService(SeatPort seatPort) {
        this.seatPort = seatPort;
    }

    @Override
    public GetAvailableDateResult getAvailableDates() {
        List<LocalDate> dates = seatPort.findAvailableDates();
        return new GetAvailableDateResult(dates);
    }

    @Override
    public GetSeatsResult getSeats(LocalDate date) {
        List<SeatView> seats = seatPort.findByDate(date).stream()
                .map(s -> new SeatView(
                        s.getSeatId(),
                        s.getDate(),
                        s.getSeatNo(),
                        s.getStatus().name(),
                        s.getHoldExpriesAt())
                ).toList();

        return new GetSeatsResult(seats);
    }
}
