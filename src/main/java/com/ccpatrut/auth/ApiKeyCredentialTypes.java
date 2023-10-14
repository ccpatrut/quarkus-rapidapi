package com.ccpatrut.auth;

import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.BaseAuthenticationRequest;

public class ApiKeyCredentialTypes extends BaseAuthenticationRequest implements AuthenticationRequest {

    private final ApiKeyCredential apiKeyCredential;

    public ApiKeyCredentialTypes(final ApiKeyCredential apiKeyCredential) {
        this.apiKeyCredential = apiKeyCredential;
    }

    public ApiKeyCredential getToken() {
        return apiKeyCredential;
    }
}
