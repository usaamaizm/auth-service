package com.hbl.authserver.controller;

import com.hbl.authserver.dto.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PreAuthorize("hasRole('USER') && hasAuthority('CAN_DELETE_USERS')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> secureApi() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         UserPrincipal principal= (UserPrincipal) auth.getPrincipal();
        return ResponseEntity.ok("Welcome " + principal.getUsername());
    }
}
