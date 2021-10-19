package myShop.Models.DBModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ms_session")
public class Session {
    public Session() {
    }

    @EmbeddedId
    private SessionCompositeKey sessionId;

    @JsonProperty("createdAt")
    @Column(name = "CREATED_AT")
    private ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC);

    public SessionCompositeKey getSessionId() {
        return this.sessionId;
    }

    public Session(SessionCompositeKey sessionId) {
        this.sessionId = sessionId;
    }

    public Session(int userId, String refreshToken) {
        this.sessionId = new SessionCompositeKey(userId, refreshToken);
    }
}
