package kr.hhplus.be.server.concert.usecase;

import kr.hhplus.be.server.concert.domain.model.Seat;
import kr.hhplus.be.server.concert.port.in.HoldSeatUseCase;
import kr.hhplus.be.server.concert.port.out.HoldPolicyPort;
import kr.hhplus.be.server.concert.port.out.QueueTokenPort;
import kr.hhplus.be.server.concert.port.out.SeatPort;
import kr.hhplus.be.server.concert.port.out.TimeProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class HoldSeatServiceTest {

    @Test
    @DisplayName("다른 사용자가 선점한 좌석 예약 시 실패")
    void hold_shouldFail_whenSeatHeldByAnotherUser() {
        //given
        SeatPort seatPort = mock(SeatPort.class);
        QueueTokenPort queueTokenPort = mock(QueueTokenPort.class);
        TimeProvider timeProvider = mock(TimeProvider.class);
        HoldPolicyPort holdPolicyPort = () -> Duration.ofMinutes(5);

        Instant now = Instant.parse("2026-02-01T10:00:00Z");
        when(timeProvider.now()).thenReturn(now);

        when(queueTokenPort.requireActiveForUpdate("t1"))
                .thenReturn(new QueueTokenPort.ActiveToken("t1", "user-A"));

        Seat seat = new Seat(
                1L,
                LocalDate.parse("2026-02-10"),
                10,
                Seat.Status.HELD,
                "user-B",
                now.plusSeconds(200),
                null
        );
        when(seatPort.loadForUpdate(LocalDate.parse("2026-02-10"), 10)).thenReturn(Optional.of(seat));

        HoldSeatService service = new HoldSeatService(seatPort, queueTokenPort, holdPolicyPort, timeProvider);

        //when & then
        assertThatThrownBy(() -> service.hold(new HoldSeatUseCase.HoldSeatCommand("t1", LocalDate.parse("2026-02-10"), 10)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("seat is not available");

        verify(seatPort, never()).save(any());
    }

    @Test
    @DisplayName("좌석 만료 상태면 hold 해제 후 선점.")
    void hold_shouldReleaseExpiredHold_andHoldForRequester() {
        //given
        SeatPort seatPort = mock(SeatPort.class);
        QueueTokenPort queueTokenPort = mock(QueueTokenPort.class);
        TimeProvider timeProvider = mock(TimeProvider.class);
        HoldPolicyPort holdPolicyPort = () -> Duration.ofMinutes(5);

        Instant now = Instant.parse("2026-02-01T10:00:00Z");
        when(timeProvider.now()).thenReturn(now);

        when(queueTokenPort.requireActiveForUpdate("t1"))
                .thenReturn(new QueueTokenPort.ActiveToken("t1", "user-A"));


        //expired HELD
        Seat seat = new Seat(
                1L,
                LocalDate.parse("2026-02-10"),
                10,
                Seat.Status.HELD,
                "user-B",
                now.minusSeconds(1), //만료
                null
        );
        when(seatPort.loadForUpdate(LocalDate.parse("2026-02-10"), 10)).thenReturn(Optional.of(seat));
        when(seatPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        HoldSeatService service = new HoldSeatService(seatPort, queueTokenPort, holdPolicyPort, timeProvider);

        //when
        HoldSeatUseCase.HoldSeatResult result = service.hold(new HoldSeatUseCase.HoldSeatCommand("t1", LocalDate.parse("2026-02-10"), 10));

        //then
        assertThat(result.status()).isEqualTo(Seat.Status.HELD.toString());
        assertThat(result.heldByUserUuid()).isEqualTo("user-A");
        assertThat(result.holdExpriesAt()).isEqualTo(now.plus(Duration.ofMinutes(5)).toString());
    }

}