package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_point")
public class UserPointEntity {
    @Id
    @Column(name="user_uuid", length = 36)
    public String userUuid;

    @Column(nullable = false)
    public long balance;

}
