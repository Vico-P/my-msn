package com.mymsn.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mymsn.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByLogin(String login);
}
