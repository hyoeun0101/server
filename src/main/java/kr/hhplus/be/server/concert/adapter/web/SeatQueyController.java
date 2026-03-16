package kr.hhplus.be.server.concert.adapter.web;

import kr.hhplus.be.server.concert.port.in.SeatQueryUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/concerts")
public class SeatQueyController {
    private final SeatQueryUseCase seatQueryUseCase;

    public SeatQueyController(SeatQueryUseCase seatQueryUseCase) {
        this.seatQueryUseCase = seatQueryUseCase;
    }

    @GetMapping("/available-dates")
    public SeatQueryUseCase.GetAvailableDateResult getAvailableDates() {
        return seatQueryUseCase.getAvailableDates();
    }

    @GetMapping("/{date}/seats")
    public SeatQueryUseCase.GetSeatsResult getSeats(@PathVariable LocalDate date) {
        return seatQueryUseCase.getSeats(date);
    }


}
