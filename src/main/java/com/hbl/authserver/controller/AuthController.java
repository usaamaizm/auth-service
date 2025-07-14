package com.hbl.authserver.controller;


import com.hbl.authserver.dto.LoginRequest;
import com.hbl.authserver.dto.SignupRequest;
import com.hbl.authserver.dto.TokenResponse;
import com.hbl.authserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequest request){
        authService.signUp(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        String response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
