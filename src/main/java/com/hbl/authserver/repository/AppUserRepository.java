package com.hbl.authserver.repository;

import com.hbl.authserver.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    AppUser findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
