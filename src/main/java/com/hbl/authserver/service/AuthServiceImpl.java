package com.hbl.authserver.service;

import com.hbl.authserver.dto.LoginRequest;
import com.hbl.authserver.dto.SignupRequest;
import com.hbl.authserver.entity.AppUser;
import com.hbl.authserver.entity.Role;
import com.hbl.authserver.entity.UserRole;
import com.hbl.authserver.enums.Roles;
import com.hbl.authserver.repository.AppUserRepository;
import com.hbl.authserver.repository.RoleRepository;
import com.hbl.authserver.repository.UserRoleRepository;
import com.hbl.authserver.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthorityServiceImpl userAuthorityService;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;


    @Override
    public void signUp(SignupRequest request) {
        if (adminUserRepository.existsByUsername(request.getUsername()) || adminUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Username or Email is already taken");
        }
        AppUser user = AppUser.builder()
                .uuid(UUID.randomUUID().toString())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        adminUserRepository.save(user);

        Role role = roleRepository.findByName(Roles.USER.name());

        UserRole userRole = UserRole.builder()
                .userUuid(user.getUuid())
                .roleUuid(role.getUuid())
                .roleName(Roles.USER.name())
                .build();
        userRoleRepository.save(userRole);
    }

    @Override
    public String login(LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Username or Password is null");
        }

        AppUser user = adminUserRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        List<String> authorities = userAuthorityService.getUserAuthorities(user.getUuid());
        return jwtTokenProvider.generateToken(user, authorities);
    }
}
