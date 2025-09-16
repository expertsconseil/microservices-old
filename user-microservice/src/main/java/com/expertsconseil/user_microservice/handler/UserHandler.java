package com.expertsconseil.user_microservice.handler;


import com.expertsconseil.user_microservice.config.exception.UserDataException;
import com.expertsconseil.user_microservice.config.exception.UserNotFoundException;
import com.expertsconseil.user_microservice.models.db.User;
import com.expertsconseil.user_microservice.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Validator validator;

    Sinks.Many<User> UserSink = Sinks.many().replay().latest();

    @SneakyThrows
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> saveUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .doOnNext(this::validate)
                .flatMap(userRepository::save)
                .doOnNext(savedUser -> UserSink.tryEmitNext(savedUser))
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    private void validate(User User) {
        var constraintViolations = validator.validate(User);
        if (!constraintViolations.isEmpty()) {
            log.info("constraintViolations : {}", constraintViolations);
            var errorMessage = constraintViolations
                    .stream().map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new UserDataException(errorMessage);
        }

    }

   @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
       return serverRequest.queryParam("id")
               .map(s -> ServerResponse.ok().body(userRepository.findById(s), User.class))
               .orElseGet(() -> ServerResponse.ok().body(userRepository.findAll(), User.class))
               .log();
      //  var userFlux = userRepository.findAll();
        //return ServerResponse.ok().body(userFlux, User.class);
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        var existingUser =  userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found for the given user id " + id)));
        return existingUser
                .flatMap(foundUser -> serverRequest.bodyToMono(User.class)
                        .map(User -> {
                            foundUser.setFirstName(User.getFirstName());
                            foundUser.setLastName(User.getLastName());
                            foundUser.setEmail(User.getEmail());
                            foundUser.setBirthday(User.getBirthday());
                            foundUser.setAddress(User.getAddress());
                            foundUser.setUsername(User.getUsername());
                            foundUser.setPassword(User.getPassword());
                            foundUser.setUpdatedAt(LocalDateTime.now());
                            return foundUser;
                        })
                        .flatMap(userRepository::save)
                        .flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser))
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        var existingUser = userRepository.findById(id);
        return existingUser.flatMap(User -> userRepository.deleteById(id))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getUsersStream(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(UserSink.asFlux(), User.class)
                .log();
    }

    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        var user = userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found for the given user Id " + id)));
        return ServerResponse.ok().body(user, User.class);
    }

    public Mono<ServerResponse> findByUsername(ServerRequest serverRequest) {
        var username = serverRequest.pathVariable("username");
        var user = userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found for the given username " + username)));
        return ServerResponse.ok().body(user, User.class);
    }
}

