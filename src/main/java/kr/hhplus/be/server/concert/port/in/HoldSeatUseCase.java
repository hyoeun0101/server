package kr.hhplus.be.server.concert.port.in;

import java.time.LocalDate;

public interface HoldSeatUseCase {
    HoldSeatResult hold(HoldSeatCommand command);

    record HoldSeatCommand(String quequeTokenId, LocalDate date, int seatNo) {}

    record HoldSeatResult(Long seatId, String status, String heldByUserUuid, String holdExpriesAt) {}
}
