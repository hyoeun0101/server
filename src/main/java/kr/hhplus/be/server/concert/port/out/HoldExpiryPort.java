package kr.hhplus.be.server.concert.port.out;

import java.time.Instant;

public interface HoldExpiryPort {
    // 만료된 좌석 HOLD를 AVAILABLE로 변환.
    // 멱등 : 같은 시각에 여러 번 실행해도 결과가 동일하다.
    int releaseExpiredHolds(Instant now);
}
