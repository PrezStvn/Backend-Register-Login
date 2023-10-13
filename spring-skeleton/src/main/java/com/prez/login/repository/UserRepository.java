package com.prez.login.repository;

import com.prez.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // At this point, you can stick with the methods provided by JpaRepository.
    // But, as an example, if you want to find a user by username:
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}