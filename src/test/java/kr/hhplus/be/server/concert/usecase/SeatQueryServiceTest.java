package kr.hhplus.be.server.concert.usecase;

import kr.hhplus.be.server.concert.domain.model.Seat;
import kr.hhplus.be.server.concert.port.in.SeatQueryUseCase;
import kr.hhplus.be.server.concert.port.out.SeatPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatQueryServiceTest {
    @InjectMocks
    SeatQueryService service;

    @Mock
    SeatPort seatPort;

    @Test
    void getSeats_shouldReturnSeats() {
        //given
        LocalDate date = LocalDate.parse("2026-02-10");
        when(seatPort.findByDate(date)).thenReturn(List.of(
                new Seat(1L, date, 1, Seat.Status.AVAILABLE, null, null, null),
                new Seat(2L, date, 2, Seat.Status.HELD, "user-A", Instant.parse("2026-02-01T10:00:00Z"), null)
        ));

        //when
        SeatQueryUseCase.GetSeatsResult result = service.getSeats(date);

        //then
        assertThat(result.seats()).hasSize(2);
        assertThat(result.seats().get(0).seatNo()).isEqualTo(1);
        assertThat(result.seats().get(1).seatNo()).isEqualTo(2);

    }

}