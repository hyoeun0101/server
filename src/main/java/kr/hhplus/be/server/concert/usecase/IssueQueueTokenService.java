package kr.hhplus.be.server.concert.usecase;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.concert.domain.model.QueueToken;
import kr.hhplus.be.server.concert.port.in.IssueQueueTokenUseCase;
import kr.hhplus.be.server.concert.port.out.QueuePolicyPort;
import kr.hhplus.be.server.concert.port.out.QueueTokenPort;
import kr.hhplus.be.server.concert.port.out.TimeProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class IssueQueueTokenService implements IssueQueueTokenUseCase {
    public final TimeProvider timeProvider;
    private final QueueTokenPort queueTokenPort;
    private final QueuePolicyPort queuePolicyPort;

    public IssueQueueTokenService(QueueTokenPort queueTokenPort, QueuePolicyPort queuePolicyPort, TimeProvider timeProvider) {
        this.queueTokenPort = queueTokenPort;
        this.queuePolicyPort = queuePolicyPort;
        this.timeProvider = timeProvider;
    }

    @Override
    @Transactional
    public IssueResult issueToken(IssueCommand command) {
        Instant now = timeProvider.now();
        String tokenId = UUID.randomUUID().toString();

        long activeCount = queueTokenPort.countActive();

        QueueToken.Status status = activeCount < queuePolicyPort.maxActiveUsers() ? QueueToken.Status.ACTIVE : QueueToken.Status.WAITING;

        QueueToken queueToken = new QueueToken(tokenId,
                command.userUuid(),
                now,
                status,
                status == QueueToken.Status.ACTIVE ? now.plus(queuePolicyPort.activeTtl()) : null
        );
        queueTokenPort.save(queueToken);

        return new IssueResult(tokenId, status.name());
    }
}
