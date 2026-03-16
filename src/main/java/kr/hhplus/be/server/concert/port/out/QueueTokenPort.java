package kr.hhplus.be.server.concert.port.out;

import kr.hhplus.be.server.concert.domain.model.QueueToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface QueueTokenPort {
    ActiveToken requireActiveForUpdate(String tokenId);

    void expireToken(String tokenId);

    record ActiveToken(String tokenId, String userUuid) {}

    QueueToken save(QueueToken token);

    long countActive();

    List<QueueToken> findWaitingOrdered();

    Optional<QueueToken> loadForUpdate(String tokenId);

    int countWaitingBefore(Instant createdAt);
}
