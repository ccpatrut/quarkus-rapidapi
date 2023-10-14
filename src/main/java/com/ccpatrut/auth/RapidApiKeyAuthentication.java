package com.ccpatrut.auth;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Collections;
import java.util.Set;

@Alternative
@ApplicationScoped
@Priority(1)
public class RapidApiKeyAuthentication implements HttpAuthenticationMechanism {
    private static final String X_RAPID_API_HEADER = "X-RapidAPI-Proxy-Secret";
    protected static final ChallengeData UNAUTHORIZED_CHALLENGE = new ChallengeData(
            HttpResponseStatus.UNAUTHORIZED.code(),
            HttpHeaderNames.WWW_AUTHENTICATE, X_RAPID_API_HEADER);
    private static final HttpCredentialTransport OIDC_SERVICE_TRANSPORT = new HttpCredentialTransport(
            HttpCredentialTransport.Type.OTHER_HEADER, X_RAPID_API_HEADER);

    private final String rapidApiKey;

    public RapidApiKeyAuthentication(@ConfigProperty(name = "RAPID_API_KEY") final String rapidApiKey) {
        this.rapidApiKey = rapidApiKey;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(final RoutingContext context,
            final IdentityProviderManager identityProviderManager) {
        final String rapidApiHeader = context.request().getHeader("X-RapidAPI-Proxy-Secret");
        if (rapidApiKey.equals(rapidApiHeader)) {
            return Uni.createFrom().item(new QuarkusSecurityIdentity.Builder()
                    .setPrincipal(new QuarkusPrincipal(rapidApiHeader)).build());
        }
        return Uni.createFrom().nullItem();

    }

    @Override
    public Uni<ChallengeData> getChallenge(final RoutingContext context) {
        return Uni.createFrom().item(UNAUTHORIZED_CHALLENGE);
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return Collections.singleton(ApiKeyCredentialTypes.class);
    }

    @Override
    public HttpCredentialTransport getCredentialTransport() {
        return OIDC_SERVICE_TRANSPORT;
    }

}