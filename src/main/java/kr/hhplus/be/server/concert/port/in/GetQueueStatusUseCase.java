package kr.hhplus.be.server.concert.port.in;

public interface GetQueueStatusUseCase {
    StatusResult getTokenStatus(String tokenId);

    record StatusResult(String tokenId, String status, Integer waitingPosition, Long estimatedWaitSeconds) {}

}
