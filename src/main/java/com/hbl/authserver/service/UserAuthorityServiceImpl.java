package com.hbl.authserver.service;

import com.hbl.authserver.entity.Permission;
import com.hbl.authserver.entity.RolePermission;
import com.hbl.authserver.entity.UserRole;
import com.hbl.authserver.repository.PermissionRepository;
import com.hbl.authserver.repository.RolePermissionRepository;
import com.hbl.authserver.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAuthorityServiceImpl implements UserAuthorityService {

    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<String> getUserAuthorities(String userUuid) {

        Set<String> authorities = new HashSet<>();
        List<UserRole> userRoles = userRoleRepository.findByUserUuid(userUuid);

        List<String> roleUuids = userRoles.stream()
                .map(UserRole::getRoleUuid)
                .toList();

        List<String> roleAuthorities = userRoles.stream()
                .map(ur -> "ROLE_" + ur.getRoleName())
                .toList();
        authorities.addAll(roleAuthorities);

        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleUuidIn(roleUuids);
        List<String> permissionUuids = rolePermissions.stream()
                .map(RolePermission::getPermissionUuid)
                .toList();

        List<Permission> permissions = permissionRepository.findByUuidIn(permissionUuids);
        List<String> permissionNames = permissions.stream()
                .map(Permission::getName)
                .toList();
        authorities.addAll(permissionNames);
        return authorities.stream().toList();
    }
}
