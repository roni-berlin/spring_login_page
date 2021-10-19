package myShop.Services;

import myShop.Models.DBModels.Session;
import myShop.Models.DBModels.SessionCompositeKey;
import myShop.Repositories.SessionRepository;
import myShop.middleware.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static myShop.Controllers.SessionController.SESSION_EXPIRATION_TIME_IN_DAYS;

@Service
public class SessionService {
    @Autowired
    SessionRepository sessionRepository;

    public void addSession(Session session) {
        sessionRepository.save(session);
    }

    public boolean verifySession(String refreshToken, String userId) {
        return this.sessionRepository.findBySessionIdUserIdAndSessionIdRefreshToken(Integer.parseInt(userId), refreshToken).isPresent();
    }

    public void clearExpiredSessions() {
        this.sessionRepository.deleteByCreatedAtBefore(ZonedDateTime.now(ZoneOffset.UTC).minusDays(SESSION_EXPIRATION_TIME_IN_DAYS));
    }

    public String refreshToken(String refreshToken, String userId) throws Exception {
        this.clearExpiredSessions();
        if (this.verifySession(refreshToken, userId)) {
            return auth.signAccessTokenFromUserID(userId);
        }else{
            throw new Exception();
        }
    }
}
