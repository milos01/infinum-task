package com.example.infinum.repository;

import com.example.infinum.domain.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long>{
    Optional<User> findByEmailAndActiveTrue(String email);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
