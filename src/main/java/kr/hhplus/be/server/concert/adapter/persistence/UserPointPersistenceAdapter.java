package kr.hhplus.be.server.concert.adapter.persistence;

import kr.hhplus.be.server.concert.adapter.persistence.jpa.UserPointEntity;
import kr.hhplus.be.server.concert.adapter.persistence.jpa.UserPointJpaRepository;
import kr.hhplus.be.server.concert.port.out.UserPointPort;
import org.springframework.stereotype.Component;

@Component
public class UserPointPersistenceAdapter implements UserPointPort {
    private final UserPointJpaRepository repo;

    public UserPointPersistenceAdapter(UserPointJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserPointAccount loadForUpdate(String userUuid) {
        UserPointEntity e = repo.lockByUserUuid(userUuid)
                .orElseThrow(() -> new IllegalStateException("point account not found"));

        return new UserPointAccount(e.userUuid, e.balance);
    }

    @Override
    public void save(UserPointAccount account) {
        UserPointEntity e = repo.findById(account.userUuid()).orElseGet(UserPointEntity::new);
        e.userUuid = account.userUuid();
        e.balance = account.balance();
        repo.save(e);
    }
}
