package kr.hhplus.be.server.concert.port.in;

public interface IssueQueueTokenUseCase {
    IssueResult issueToken(IssueCommand command);

    record IssueCommand(String userUuid) {}

    record IssueResult(String tokenId, String status) {}
}
