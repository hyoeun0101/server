package kr.hhplus.be.server.concert.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Seat {
    public enum Status {AVAILABLE, HELD, CONFIRMED}

    private final Long seatId;
    private final LocalDate date;
    private final int seatNo; // 1~50번

    private Status status;
    private String heldByUserUusid;
    private Instant holdExpriesAt;
    private String confirmedByUserUusid;

    public Seat(Long seatId, LocalDate date, int seatNo, Status status
    , String heldByUserUusid, Instant holdExpriesAt, String confirmedByUserUusid) {
        this.seatId = Objects.requireNonNull(seatId);
        this.date = Objects.requireNonNull(date);
        if (seatNo < 1 || seatNo > 50) throw new IllegalArgumentException("seatNo must be 1...50");
        this.seatNo = seatNo;

        this.status = status;
        this.heldByUserUusid = heldByUserUusid;
        this.holdExpriesAt = holdExpriesAt;
        this.confirmedByUserUusid = confirmedByUserUusid;
    }

    public Long getSeatId() {
        return seatId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public Status getStatus() {
        return status;
    }

    public String getHeldByUserUusid() {
        return heldByUserUusid;
    }

    public Instant getHoldExpriesAt() {
        return holdExpriesAt;
    }

    public String getConfirmedByUserUusid() {
        return confirmedByUserUusid;
    }



    public boolean isHoldExpired(Instant now) {
        return status == Status.HELD && holdExpriesAt != null && holdExpriesAt.isBefore(now);
    }

    public void releaseHold() {
        status = Status.AVAILABLE;
        heldByUserUusid = null;
        holdExpriesAt = null;
    }

    public void hold(String userUuid, Instant expiresAt) {
        status = Status.HELD;
        heldByUserUusid = userUuid;
        holdExpriesAt = expiresAt;
        confirmedByUserUusid = null;
    }

    public void confirm(String userUuid) {
        status = Status.CONFIRMED;
        confirmedByUserUusid = userUuid;
        heldByUserUusid = userUuid;
        holdExpriesAt = null;
    }

    public boolean isValidHeld(String userUuid) {
        return status == Status.HELD && heldByUserUusid != null && heldByUserUusid.equals(userUuid);
    }

    public boolean isValidConfirmed(String userUuid) {
        return status == Status.CONFIRMED && confirmedByUserUusid != null && confirmedByUserUusid.equals(userUuid);
    }


}
