package com.ccpatrut.auth;

import io.quarkus.oidc.runtime.OidcAuthenticationMechanism;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Alternative
@Priority(1)
@ApplicationScoped
@Slf4j
@AllArgsConstructor
public class AuthMechanism implements HttpAuthenticationMechanism {

    private final OidcAuthenticationMechanism oidc;
    private final RapidApiKeyAuthentication rapidApiKeyAuthentication;

    @Override
    public Uni<SecurityIdentity> authenticate(final RoutingContext context,
            final IdentityProviderManager identityProviderManager) {
        return selectBetweenRapidApiAndOidc(context).authenticate(context, identityProviderManager);
    }

    @Override
    public Uni<ChallengeData> getChallenge(final RoutingContext context) {
        return selectBetweenRapidApiAndOidc(context).getChallenge(context);
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return new HashSet<>(oidc.getCredentialTypes());
    }

    private HttpAuthenticationMechanism selectBetweenRapidApiAndOidc(final RoutingContext context) {
        // for example, if no `Authorization` header is available and no `code`
        // parameter is provided - use `jwt` to create a challenge
        final String header = context.request().getHeader("X-RapidAPI-Proxy-Secret");
        return header != null ? rapidApiKeyAuthentication : oidc;

    }
}
