package com.google.sps.data;

public final class Profile {
    
    private final String email;
    private final String logoutUrl;
    private final String loginUrl;

    public Profile(String email, String logoutUrl, String loginUrl) {
        this.email = email;
        this.logoutUrl = logoutUrl;
        this.loginUrl = loginUrl;
    }
}
