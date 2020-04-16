package financeTracker.ch.pesrsistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByCredentials(@Param("email") String email, @Param("password") String password);
}
