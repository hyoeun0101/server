package kr.hhplus.be.server.concert.adapter.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.concert.port.in.HoldSeatUseCase;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final HoldSeatUseCase holdSeatUseCase;


    public ReservationController(HoldSeatUseCase holdSeatUseCase) {
        this.holdSeatUseCase = holdSeatUseCase;
    }

    record HoldReqeust(@NotNull LocalDate date, @Min(1) @Max(50) int seatNo) {}

    @PostMapping("/hold")
    public HoldSeatUseCase.HoldSeatResult hold(
            @RequestHeader("X-QUEUE-TOKEN") String tokenId,
            @RequestBody HoldReqeust req
    ) {
        return holdSeatUseCase.hold(new HoldSeatUseCase.HoldSeatCommand(tokenId, req.date, req.seatNo));
    }
}
