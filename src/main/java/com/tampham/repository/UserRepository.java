package com.tampham.repository;

import com.tampham.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean findByUsernameOrEmail(String username, String email);
    User findByIdAndVerifyCode(Long id, String code);
    Optional<User> findByUsername(String username);
}
