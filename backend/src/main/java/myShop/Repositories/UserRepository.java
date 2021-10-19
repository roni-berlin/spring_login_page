package myShop.Repositories;

import myShop.Models.DBModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User.userPassword findPasswordByEmail(String email);
    User.userId findUserIdByEmail(String email);
}
