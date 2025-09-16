package com.expertsconseil.user_microservice.handler;

import com.expertsconseil.user_microservice.models.request.LoginRequest;
import com.expertsconseil.user_microservice.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Slf4j
@Component
public class LoginHandler {

    @Autowired
    LoginService loginService;



    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginRequest> requestMono = request.bodyToMono(LoginRequest.class);
        return requestMono
                        .flatMap(loginRequest -> loginService.findByUserName(loginRequest.getUsername())
                                .flatMap(userDetails -> {
                                    if(loginService.checkPassword(loginRequest.getPassword(), userDetails.getPassword())){
                                        return Mono.just(userDetails);
                                    }
                                    throw new BadCredentialsException("Password incorrect for username: " + loginRequest.getUsername());
                                })
                                .flatMap(userDetails -> loginService.checkUserDetails(userDetails)))
                .flatMap(loginResponse -> ServerResponse.ok().bodyValue(loginResponse))
                .onErrorResume(loginService::sendErrorResponse);
    }

    public Mono<ServerResponse> auth(ServerRequest serverRequest) {
        return ok().bodyValue(Mono.just("Hello from Auth"));
    }


}
