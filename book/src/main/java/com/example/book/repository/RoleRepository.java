package com.example.book.repository;

import com.example.book.domain.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<AppUserRole,Long> {
}
