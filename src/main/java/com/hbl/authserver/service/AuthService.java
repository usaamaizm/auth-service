package com.hbl.authserver.service;

import com.hbl.authserver.dto.LoginRequest;
import com.hbl.authserver.dto.SignupRequest;

public interface AuthService {

    void signUp(SignupRequest signupRequest);

    String login(LoginRequest request);
}
