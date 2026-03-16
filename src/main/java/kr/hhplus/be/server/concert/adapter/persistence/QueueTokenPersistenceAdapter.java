package kr.hhplus.be.server.concert.adapter.persistence;

import kr.hhplus.be.server.concert.adapter.persistence.jpa.QueueTokenEntity;
import kr.hhplus.be.server.concert.adapter.persistence.jpa.QueueTokenJpaRepository;
import kr.hhplus.be.server.concert.domain.model.QueueToken;
import kr.hhplus.be.server.concert.port.out.QueueTokenPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class QueueTokenPersistenceAdapter implements QueueTokenPort {
    private final QueueTokenJpaRepository repo;

    public QueueTokenPersistenceAdapter(QueueTokenJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public ActiveToken requireActiveForUpdate(String tokenId) {
        QueueTokenEntity e = repo.lockByTokenId(tokenId)
                .orElseThrow(() -> new SecurityException("invalid token"));

        if (!"ACTIVE".equals(e.status)) {
            throw new SecurityException("token is not active");
        }
        if (e.expiresAt != null && e.expiresAt.isBefore(Instant.now())) {
            e.status = "EXPIRED";
            e.expiresAt = null;
            repo.save(e);
            throw new SecurityException("token expired");
        }

        return new ActiveToken(e.tokenId, e.userUuid);
    }

    @Override
    public void expireToken(String tokenId) {
        QueueTokenEntity e = repo.lockByTokenId(tokenId)
                .orElseThrow(() -> new SecurityException("invalid token"));
        e.status = "EXPIRED";
        e.expiresAt = null;
        repo.save(e);
    }

    @Override
    public QueueToken save(QueueToken token) {
        QueueTokenEntity e = repo.findById(token.getTokenId()).orElse(new QueueTokenEntity());
        e.tokenId = token.getTokenId();
        e.userUuid = token.getUserUuid();
        e.status = token.getStatus().name();
        e.createdAt = token.getCreatedAt();
        e.expiresAt = token.getExpiresAt();

        repo.save(e);

        return token;
    }

    @Override
    public long countActive() {
        return repo.countByStatus("ACTIVE");
    }

    @Override
    public List<QueueToken> findWaitingOrdered() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<QueueToken> loadForUpdate(String tokenId) {
        return repo.lockByTokenId(tokenId).map(this::toDomain);
    }

    @Override
    public int countWaitingBefore(Instant createdAt) {
        return repo.countWaitingBefore(createdAt);
    }

    private QueueToken toDomain(QueueTokenEntity e) {
        return new QueueToken(e.tokenId,
                e.userUuid,
                e.createdAt,
                QueueToken.Status.valueOf(e.status),
                e.expiresAt
        );
    }
}
