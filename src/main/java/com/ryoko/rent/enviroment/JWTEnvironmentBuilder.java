package com.ryoko.rent.enviroment;

import lombok.Data;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Data
public class JWTEnvironmentBuilder {
    private final String TOKEN_PREFIX;
    private final String CLIENT_IP;
    private final String SECRET;
    private final String TOKEN_CANNOT_BE_VERIFIED;
    private final String ISSUER;
    private final String AUDIENCE;
    private final Long EXPIRATION_TIME;
    private final String JWT_TOKEN_HEADER;

    public JWTEnvironmentBuilder(Environment environment) {
        this.TOKEN_PREFIX = environment.getRequiredProperty("security.token.token-prefix");
        this.CLIENT_IP = environment.getRequiredProperty("security.token.client-ip");
        this.SECRET = environment.getRequiredProperty("security.token.secret");
        this.ISSUER = environment.getRequiredProperty("security.token.issuer");
        this.TOKEN_CANNOT_BE_VERIFIED = environment.getRequiredProperty("security.token.token-cannot-be-verified");
        this.AUDIENCE = environment.getRequiredProperty("security.token.audience");
        this.EXPIRATION_TIME = Long.parseLong(environment.getRequiredProperty("security.token.expiration-time"));
        this.JWT_TOKEN_HEADER = environment.getRequiredProperty("security.token.jwt-token-header");
    }
}
