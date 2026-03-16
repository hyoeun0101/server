package kr.hhplus.be.server.concert.port.out;

public interface UserPointPort {

    UserPointAccount loadForUpdate(String userUuid);

    void save(UserPointAccount account);

//    Optional<UserPointAccount> findByUserUuid(String userUuid);

    record UserPointAccount(String userUuid, long balance) {
        public UserPointAccount charge(long amount) {
            if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
            return new UserPointAccount(userUuid, balance + amount);
        }

        public UserPointAccount spend(long amount) {
            if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
            if (balance < amount) throw new IllegalStateException("insufficient");

            return new UserPointAccount(userUuid, balance - amount);
        }
    }
}
