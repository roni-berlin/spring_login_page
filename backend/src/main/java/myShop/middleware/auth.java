package myShop.middleware;


import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import myShop.Models.DBModels.SessionCompositeKey;

import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;

import static myShop.Controllers.SessionController.REFRESH_TOKEN_EXPIRATION_TIME_IN_DAYS;

public class auth {
    private static final String secret = "secret";
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateRefreshToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String signAccessTokenFromUserID(String userID) {
        Signer signer = HMACSigner.newSHA256Signer(secret);
        JWT jwt = new JWT()
                .setSubject(userID)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusDays(REFRESH_TOKEN_EXPIRATION_TIME_IN_DAYS));
        return JWT.getEncoder().encode(jwt, signer);
    }

    public static String verifyUserIDFromAccessToken(String accessToken) {
        Verifier verifier = HMACVerifier.newVerifier(secret);
        JWT jwt = JWT.getDecoder().decode(accessToken, verifier);
        return jwt.subject;
    }
}
