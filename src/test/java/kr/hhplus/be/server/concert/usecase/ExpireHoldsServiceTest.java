package kr.hhplus.be.server.concert.usecase;

import kr.hhplus.be.server.concert.port.in.ExpireHoldUseCase;
import kr.hhplus.be.server.concert.port.out.HoldExpiryPort;
import kr.hhplus.be.server.concert.port.out.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpireHoldsServiceTest {
    @InjectMocks
    ExpireHoldsService expireHoldsService;

    @Mock
    HoldExpiryPort holdExpiryPort;
    @Mock
    TimeProvider timeProvider;

    @Test
    void expireNow_shouldCallPort_andReturnReleasedCount() {
        //given
        Instant now = Instant.parse("2026-02-01T10:00:00Z");
        when(timeProvider.now()).thenReturn(now);
        when(holdExpiryPort.releaseExpiredHolds(now)).thenReturn(7);

        //when
        ExpireHoldUseCase.ExpireResult result = expireHoldsService.expireNow();

        //then
        assertThat(result.releasedCount()).isEqualTo(7);
        assertThat(result.ranAt()).isEqualTo(now);
        verify(holdExpiryPort).releaseExpiredHolds(now);
    }

}