package myShop.Controllers;

import myShop.Models.DBModels.Session;
import myShop.Models.DBModels.SessionCompositeKey;
import myShop.Services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/sessions")
public class SessionController {
    public final static int REFRESH_TOKEN_EXPIRATION_TIME_IN_DAYS = 7;
    public final static int SESSION_EXPIRATION_TIME_IN_DAYS = 30;
    final static int REFRESH_TOKEN_EXPIRATION_TIME = (int) TimeUnit.DAYS.toSeconds(REFRESH_TOKEN_EXPIRATION_TIME_IN_DAYS);
    final static int SESSION_EXPIRATION_TIME = (int) TimeUnit.DAYS.toSeconds(SESSION_EXPIRATION_TIME_IN_DAYS);
    final static String ACCESS_TOKEN_COOKIE_KEY = "accessToken";
    final static String REFRESH_TOKEN_COOKIE_KEY = "refreshToken";
    final static String ID_COOKIE_KEY = "userId";
    final static String DEFAULT_COOKIE_PATH = "/";

    @Autowired
    SessionService sessionService;

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletResponse response,
                             @CookieValue(name = "refreshToken") String refreshToken,
                             @CookieValue(name = "userId") String userId
    )  {
        try {
            String accessToken = sessionService.refreshToken(refreshToken, userId);
            Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_KEY, accessToken);
            accessTokenCookie.setPath(DEFAULT_COOKIE_PATH);
            accessTokenCookie.setMaxAge(REFRESH_TOKEN_EXPIRATION_TIME);
            response.addCookie(accessTokenCookie);
        } catch (Exception e) {
            // add logger
        }
    }
}
