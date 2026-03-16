package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "queue_token")
public class QueueTokenEntity {
    @Id
    @Column(name = "token_id", length = 36)
    public String tokenId;

    @Column(name = "user_uuid", nullable = false, length = 36)
    public String userUuid;

    @Column(nullable = false)
    public String status;

    @Column(nullable = false)
    public Instant createdAt;

    @Column(name = "expires_at")
    public Instant expiresAt;
}
