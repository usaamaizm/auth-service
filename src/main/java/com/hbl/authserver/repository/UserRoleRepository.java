package com.hbl.authserver.repository;

import com.hbl.authserver.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserUuid(String userUuid);

    Optional<UserRole> findByUserUuidAndRoleUuid(String userUuid, String roleUuid);
}
