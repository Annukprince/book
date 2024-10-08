package com.example.book.repository;

import com.example.book.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
}
