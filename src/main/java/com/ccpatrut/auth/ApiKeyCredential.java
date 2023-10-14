package com.ccpatrut.auth;

import io.quarkus.security.credential.Credential;

public class ApiKeyCredential implements Credential {

    private final String key;

    public ApiKeyCredential(final String key) {
        this.key = key;
    }

}
