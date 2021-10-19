package myShop.Repositories;

import myShop.Models.DBModels.Session;
import myShop.Models.DBModels.SessionCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, SessionCompositeKey> {
    Optional<Session> findBySessionIdUserIdAndSessionIdRefreshToken(int userId, String refreshToken);

    @Transactional
    void deleteByCreatedAtBefore(ZonedDateTime zonedDateTime);
}
