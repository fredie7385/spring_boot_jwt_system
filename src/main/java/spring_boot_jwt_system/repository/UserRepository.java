package spring_boot_jwt_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_boot_jwt_system.model.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
}
