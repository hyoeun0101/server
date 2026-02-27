package kr.hhplus.be.server.concert.usecase;

import kr.hhplus.be.server.concert.domain.model.QueueToken;
import kr.hhplus.be.server.concert.port.in.GetQueueStatusUseCase;
import kr.hhplus.be.server.concert.port.out.QueuePolicyPort;
import kr.hhplus.be.server.concert.port.out.QueueTokenPort;
import kr.hhplus.be.server.concert.port.out.TimeProvider;

import java.time.Instant;

public class GetQeueStatusService implements GetQueueStatusUseCase {
    private final QueueTokenPort queueTokenPort;
    private final QueuePolicyPort policyPort;
    private final TimeProvider timeProvider;

    public GetQeueStatusService(QueueTokenPort queueTokenPort, QueuePolicyPort policyPort, TimeProvider timeProvider) {
        this.queueTokenPort = queueTokenPort;
        this.policyPort = policyPort;
        this.timeProvider = timeProvider;
    }

    @Override
    public StatusResult getTokenStatus(String tokenId) {
        Instant now = timeProvider.now();
        QueueToken token = queueTokenPort.loadForUpdate(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("token not found"));

        if (token.isExpired(now)) {
            token.expire();
            queueTokenPort.save(token);
            return new StatusResult(tokenId, "EXPIRED", null, null);
        }
        if (token.getStatus() == QueueToken.Status.ACTIVE) {
            return new StatusResult(tokenId, "ACTIVE", null, null);
        }

        // WAITING이면 순번 계산
        int position = queueTokenPort.countWaitingBefore(token.getCreatedAt()) + 1;
        long estimated = position * 10L;

        return new StatusResult(tokenId, "WAITING", position, estimated);
    }
}
