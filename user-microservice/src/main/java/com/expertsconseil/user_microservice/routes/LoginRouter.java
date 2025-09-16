package com.expertsconseil.user_microservice.routes;

import com.expertsconseil.user_microservice.handler.LoginHandler;
import com.expertsconseil.user_microservice.models.request.LoginRequest;
import com.expertsconseil.user_microservice.models.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouter {

    @RouterOperations({
            @RouterOperation(
                    path = "/login", produces = {APPLICATION_JSON_VALUE},
                    beanClass = LoginHandler.class,  method = RequestMethod.POST, beanMethod = "login",
                    operation = @Operation(operationId = "login", summary = "Login User", description = "Login", tags = {"login"},
                            requestBody = @RequestBody(required = true, description = "Enter Request body as Json Object", content = @Content(schema = @Schema(implementation = LoginRequest.class))),
                            responses =  {
                            @ApiResponse(responseCode = "200", description = "Login success",
                                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}),
                                    @ApiResponse(responseCode = "400", description = "BadCredentials"),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized")
                    })
            )})
    @Bean
    public RouterFunction<ServerResponse> authRoutes(LoginHandler loginHandler) {
       return route()
                .POST("/login", RequestPredicates.accept(APPLICATION_JSON), loginHandler::login)
               .GET("/auth", loginHandler::auth)
               .build();
   }
}



