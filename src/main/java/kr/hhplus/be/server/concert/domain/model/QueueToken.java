package kr.hhplus.be.server.concert.domain.model;

import java.time.Instant;

public class QueueToken {
    public enum Status {
        WAITING, ACTIVE, EXPIRED
    }
    private final String tokenId;
    private final String userUuid;
    private Status status;
    private final Instant createdAt;
    private Instant expiresAt;

    public QueueToken(String tokenId, String userUuid, Instant createdAt, Status status, Instant expiresAt) {
        this.tokenId = tokenId;
        this.userUuid = userUuid;
        this.createdAt = createdAt;
        this.status = status;
        this.expiresAt = expiresAt;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void activate(Instant expiresAt) {
        this.status = Status.ACTIVE;
        this.expiresAt = expiresAt;
    }

    public void expire() {
        this.status = Status.EXPIRED;
        this.expiresAt = null;
    }

    public boolean isExpired(Instant now) {
        return this.status == Status.ACTIVE && expiresAt != null && expiresAt.isBefore(now);
    }
}
