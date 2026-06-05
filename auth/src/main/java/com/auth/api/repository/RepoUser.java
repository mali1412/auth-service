package com.auth.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.api.entity.User;

@Repository
public interface RepoUser extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}