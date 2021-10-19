package myShop.Models.DBModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;

@Entity
@Table(name = "ms_user")
public class User {

    public User(){

    }

    public User(String firstName, String lastName, String email, String password) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPassword(password);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @JsonProperty("firstName")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @JsonProperty("lastName")
    @Column(name = "LAST_NAME")
    private String lastName;

    @JsonProperty("email")
    @Column(name = "EMAIL", unique = true)
    private String email;

    @JsonProperty("password")
    @Column(name = "PASSWORD")
    private String password;

    @PostPersist
    private void preSaveNewUser(){
        String pw_hash = BCrypt.hashpw(this.getPassword(), BCrypt.gensalt());
        this.setPassword(pw_hash);
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public interface userPassword {
        public String getPassword();
    }

    public interface userId {
        public Integer getUserId();
    }
}
