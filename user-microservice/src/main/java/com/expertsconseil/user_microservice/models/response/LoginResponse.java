package com.expertsconseil.user_microservice.models.response;

import org.springframework.security.core.userdetails.UserDetails;

public record LoginResponse(UserDetails userDetails, String token) {
}
