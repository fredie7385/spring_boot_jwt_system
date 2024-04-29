package spring_boot_jwt_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring_boot_jwt_system.model.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("""
                  Select t from Token t inner join UserModel u
                  on  t.userModel.id = u.id
                  where t.userModel.id =:userModelId and t.loggedOut = false
            """)
    List<Token> findAllTokenByUser(long userModelId);

    Optional<Token> findByToken(String token);
}
