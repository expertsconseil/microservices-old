package com.expertsconseil.user_microservice.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Getter
    private String username;
    @Getter
    private String password;
}
