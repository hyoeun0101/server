package kr.hhplus.be.server.concert.usecase;

import kr.hhplus.be.server.concert.port.in.IssueQueueTokenUseCase;
import kr.hhplus.be.server.concert.port.out.QueuePolicyPort;
import kr.hhplus.be.server.concert.port.out.QueueTokenPort;
import kr.hhplus.be.server.concert.port.out.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueQueueTokenServiceTest {
    @Mock
    QueuePolicyPort policyPort;
    @Mock
    QueueTokenPort port;

    @Mock
    TimeProvider timeProvider;

    @InjectMocks
    IssueQueueTokenService service;

    @Test
    void issue_shouldBeActive_whenBelowMax() {
        //given
        when(policyPort.maxActiveUsers()).thenReturn(100);
        when(policyPort.activeTtl()).thenReturn(Duration.ofMinutes(10));
        when(port.countActive()).thenReturn(10L);
        when(timeProvider.now()).thenReturn(Instant.now());

        //when
        IssueQueueTokenUseCase.IssueResult result = service.issueToken(new IssueQueueTokenUseCase.IssueCommand("user-A"));

        //then
        assertThat(result.status()).isEqualTo("ACTIVE");
        verify(port).save(any());

    }

}