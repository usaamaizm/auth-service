package com.hbl.authserver.dto;





public class UserPrincipal {

    private final String uuid;
    private final String username;


    public UserPrincipal(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username; // For SecurityContext.getAuthentication().getName()
    }
}
