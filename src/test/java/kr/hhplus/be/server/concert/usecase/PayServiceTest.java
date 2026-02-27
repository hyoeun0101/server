package kr.hhplus.be.server.concert.usecase;

import kr.hhplus.be.server.concert.domain.model.Seat;
import kr.hhplus.be.server.concert.port.in.PointUseCase;
import kr.hhplus.be.server.concert.port.out.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PayServiceTest {
    @Test
    void pay_shouldConfirmSeat_createPayment_andExpireToken() {
        //given
        QueueTokenPort queueTokenPort = mock(QueueTokenPort.class);
        SeatPort seatPort = mock(SeatPort.class);
        UserPointPort userPointPort = mock(UserPointPort.class);
        PaymentPort paymentPort = mock(PaymentPort.class);
        TimeProvider timeProvider = mock(TimeProvider.class);

        Instant now = Instant.parse("2026-02-01T10:00:00Z");
        when(timeProvider.now()).thenReturn(now);

        when(queueTokenPort.requireActiveForUpdate("t1"))
                .thenReturn(new QueueTokenPort.ActiveToken("t1", "user-A"));

        Seat seat = new Seat(
                10L,
                LocalDate.parse("2026-02-10"),
                7,
                Seat.Status.HELD,
                "user-A",
                now.plusSeconds(120),
                null
        );
        when(seatPort.findById(10L)).thenReturn(Optional.of(seat));
        when(userPointPort.loadForUpdate("user-A"))
                .thenReturn(new UserPointPort.UserPointAccount("user-A", 1000));

        when(paymentPort.createPaymentGetPaymentId("user-A", 10L, 300, now))
                .thenReturn(999L);

        PointService service = new PointService(seatPort, queueTokenPort, userPointPort, paymentPort, timeProvider);

        //when
        PointUseCase.PayResult result = service.pay(new PointUseCase.PayCommand("t1", 10L, 300));

        //then
        assertThat(result.paymentId()).isEqualTo(999L);
        assertThat(result.seatId()).isEqualTo(10L);
        assertThat(result.paidAt()).isEqualTo(now);
    }

    @Test
    void pay_shouldFail_whenInsufficientBalance() {
        //given
        QueueTokenPort queueTokenPort = mock(QueueTokenPort.class);
        SeatPort seatPort = mock(SeatPort.class);
        UserPointPort userPointPort = mock(UserPointPort.class);
        PaymentPort paymentPort = mock(PaymentPort.class);
        TimeProvider timeProvider = mock(TimeProvider.class);

        Instant now = Instant.parse("2026-02-01T10:00:00Z");
        String userUuid = "user-A";
        long seatId = 10L;

        when(timeProvider.now()).thenReturn(now);

        when(queueTokenPort.requireActiveForUpdate("t1"))
                .thenReturn(new QueueTokenPort.ActiveToken("t1", userUuid));

        Seat seat = new Seat(
                seatId,
                LocalDate.parse("2026-02-10"),
                7,
                Seat.Status.HELD,
                userUuid,
                now.plusSeconds(120),
                null
        );
        when(seatPort.findById(seatId)).thenReturn(Optional.of(seat));
        when(userPointPort.loadForUpdate(userUuid))
                .thenReturn(new UserPointPort.UserPointAccount(userUuid, 100));

        when(paymentPort.createPaymentGetPaymentId(userUuid, seatId, 300, now))
                .thenReturn(999L);

        PointService service = new PointService(seatPort, queueTokenPort, userPointPort, paymentPort, timeProvider);

    }

}