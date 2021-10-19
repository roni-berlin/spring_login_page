package myShop.Services;

import myShop.Repositories.SessionRepository;
import myShop.Models.DBModels.User;
import myShop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NonUniqueResultException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public String getPasswordByEmail(String email) {
        return userRepository.findPasswordByEmail(email).getPassword();
    }

    public boolean validatePassword(String givenPassword, String actualPassword) {
        return BCrypt.checkpw(givenPassword, actualPassword);
    }

    public Integer getIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email).getUserId();
    }

    public User getById(String userId) {
        return userRepository.findById(Integer.parseInt(userId)).get();
    }

    public int signIn(String email, String givenPassword) {
        String actualPassword = this.getPasswordByEmail(email);
        if (this.validatePassword(givenPassword, actualPassword)) {
            return this.getIdByEmail(email);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Or Password Are Incorrect");
        }
    }

    public void addNewUser(User user) throws NonUniqueResultException {
        userRepository.save(user);
    }


}
