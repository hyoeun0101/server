package kr.hhplus.be.server.concert.adapter.persistence.jpa;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name="concert_seat", uniqueConstraints = @UniqueConstraint(columnNames = {"concert_date", "seat_no"}))
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "concert_date", nullable = false)
    public LocalDate concertDate;

    @Column(name = "seat_no", nullable = false)
    public int seatNo;

    @Column(nullable = false)
    public String status;

    public String heldByUserUuid;

    public Instant holdExpiresAt;

    public String confirmedByUserUuid;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
