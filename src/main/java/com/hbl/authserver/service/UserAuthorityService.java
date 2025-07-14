package com.hbl.authserver.service;

import java.util.List;

public interface UserAuthorityService {
    List<String> getUserAuthorities(String userUuid);

}
