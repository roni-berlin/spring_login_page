package myShop.Controllers;

import io.fusionauth.jwt.JWTExpiredException;
import myShop.Models.DBModels.Session;
import myShop.Models.DBModels.SessionCompositeKey;
import myShop.Services.SessionService;
import myShop.middleware.auth;
import myShop.Services.UserService;
import myShop.Models.DBModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NonUniqueResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static myShop.Controllers.SessionController.*;
import static org.springframework.web.util.CookieGenerator.DEFAULT_COOKIE_PATH;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    private void createNewSessionAndCookies(int userID, HttpServletResponse response) {
        String accessToken = auth.signAccessTokenFromUserID(Integer.toString(userID));
        String refreshToken = auth.generateRefreshToken();
        sessionService.addSession(new Session(userID, refreshToken));
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_KEY, accessToken);
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_KEY, refreshToken);
        Cookie cookieId = new Cookie(ID_COOKIE_KEY, Integer.toString(userID));
        accessTokenCookie.setPath(DEFAULT_COOKIE_PATH);
        refreshTokenCookie.setPath(DEFAULT_COOKIE_PATH);
        cookieId.setPath(DEFAULT_COOKIE_PATH);
        accessTokenCookie.setMaxAge(REFRESH_TOKEN_EXPIRATION_TIME);
        refreshTokenCookie.setMaxAge(SESSION_EXPIRATION_TIME);
        cookieId.setMaxAge(SESSION_EXPIRATION_TIME);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.addCookie(cookieId);
    }

    @GetMapping("/sign-in")
    public void signIn(HttpServletResponse response,
                       @RequestParam(value = "email") String email,
                       @RequestParam(value = "password") String givenPassword
    ) throws IOException {
        try {
            int userID = userService.signIn(email, givenPassword);
            createNewSessionAndCookies(userID, response);
        } catch (ResponseStatusException e) {
            response.sendError(e.getRawStatusCode(), e.getReason());
        }
    }

    @PostMapping("/sign-up")
    public void signUp(HttpServletResponse response,
                       @RequestBody User newUser) throws IOException {
        try {
            userService.addNewUser(newUser);
        }catch (DataIntegrityViolationException e){
            response.sendError(HttpStatus.FORBIDDEN.value(), "User Already Exists");
        }
    }

    @GetMapping("/validate")
    public User validate(@CookieValue(name = "accessToken") String accessToken) {
        try {
            String userId = auth.verifyUserIDFromAccessToken(accessToken);
            return userService.getById(userId);
        } catch (JWTExpiredException e) {
            // add logger
        }
        return null;
    }
}
