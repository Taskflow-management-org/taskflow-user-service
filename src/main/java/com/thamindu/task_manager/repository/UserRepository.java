package com.thamindu.task_manager.repository;

import com.thamindu.task_manager.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(@NonNull String username);

    boolean existsByEmail(String email);

}
