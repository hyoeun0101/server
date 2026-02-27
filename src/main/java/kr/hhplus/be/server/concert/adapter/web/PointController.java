package kr.hhplus.be.server.concert.adapter.web;

import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.concert.port.in.PointUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PointController {

    private final PointUseCase pointUsecase;

    public PointController(PointUseCase payUseCase) {
        this.pointUsecase = payUseCase;
    }

    public record PayRequest(long seatId, @Positive long amount) {}

    @PostMapping("/payments")
    public PointUseCase.PayResult pay(@RequestHeader("X-QUEUE-TOKEN")String tokenId, @RequestBody PayRequest req) {
        return pointUsecase.pay(new PointUseCase.PayCommand(tokenId, req.seatId, req.amount));
    }

    public record ChargeRequest(String userUuid, long amount) {}
    @PostMapping("/points/charge")
    public PointUseCase.ChargeResult charge(@RequestHeader("X-QUEUE-TOKEN")String tokenId, @RequestBody ChargeRequest req) {
        return pointUsecase.chargePoint(new PointUseCase.ChargeCommand(tokenId, req.userUuid, req.amount));
    }

    public record GetReqeust(String userUuid) {}
    public PointUseCase.GetResult getPoint(@RequestHeader("X-QUEUE-TOKEN") String tokenId, @RequestBody GetReqeust req) {
        return pointUsecase.getPoint(new PointUseCase.GetCommand(tokenId, req.userUuid));
    }

}
