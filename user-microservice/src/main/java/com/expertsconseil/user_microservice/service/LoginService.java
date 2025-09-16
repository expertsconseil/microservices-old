package com.expertsconseil.user_microservice.service;



import com.expertsconseil.user_microservice.config.jwt.JwtService;
import com.expertsconseil.user_microservice.models.response.LoginResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class LoginService {


    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginService(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Mono<UserDetails>findByUserName(String username){
        return userDetailsService.findByUsername(username);
    }

    public Boolean checkPassword(String userDetailsPassword, String password){
        return passwordEncoder.matches(userDetailsPassword, password);
    }

    public Mono<? extends ServerResponse> sendErrorResponse(Throwable error) {
        return switch (error) {
            case UsernameNotFoundException e -> ServerResponse.badRequest().bodyValue(e.getMessage());
            case BadCredentialsException e -> ServerResponse.badRequest().bodyValue(e.getMessage());
            case DisabledException e -> ServerResponse.status(401).bodyValue(e.getMessage());
            case LockedException e -> ServerResponse.status(401).bodyValue(e.getMessage());
            case AccountExpiredException e -> ServerResponse.status(401).bodyValue(e.getMessage());
            case CredentialsExpiredException e -> ServerResponse.status(401).bodyValue(e.getMessage());
            default -> ServerResponse.status(401).build();
        };
    }

    public Mono<LoginResponse> checkUserDetails(UserDetails userDetails) {
        if(userDetails.isEnabled()
                && userDetails.isAccountNonExpired()
                && userDetails.isAccountNonLocked()
                && userDetails.isCredentialsNonExpired()){
            String token = jwtService.generateToken(userDetails);
            return Mono.just(new LoginResponse(userDetails, token));
        }
        else {
            if(!userDetails.isEnabled())
                throw new DisabledException("Account disabled");
            if (!userDetails.isAccountNonLocked())
                throw new LockedException("Account Locked");
            if(!userDetails.isAccountNonExpired())
                throw new AccountExpiredException("Account expired");
            if(!userDetails.isCredentialsNonExpired())
                throw new CredentialsExpiredException("Credentials expired");
        }
        return null;
    }
}
