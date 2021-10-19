package myShop.Models.DBModels;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SessionCompositeKey implements Serializable {
    private Integer userId;
    private String refreshToken;

    public SessionCompositeKey(){
        
    }

    public SessionCompositeKey(Integer userId, String refreshToken){
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
