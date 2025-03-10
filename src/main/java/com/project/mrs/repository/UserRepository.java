package com.project.mrs.repository;

import com.project.mrs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrUserEmail(String userName, String userEmail);
    Optional<User> findByUsername(String userName);
}
