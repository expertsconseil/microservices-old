package com.expertsconseil.user_microservice.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt-service")
@Component
@Getter
@Setter
public class JwtServiceConfiguration {

    private String secretKey;

    private long tokenExpiration;
}
