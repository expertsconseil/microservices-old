package com.expertsconseil.user_microservice.routes;

import com.expertsconseil.user_microservice.handler.UserHandler;
import com.expertsconseil.user_microservice.models.db.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {

    public static final String USER_END_POINT = "/user";
    @RouterOperations({
            @RouterOperation(
                    path = USER_END_POINT, produces = {APPLICATION_JSON_VALUE}, method = RequestMethod.GET,
                    operation = @Operation(operationId = "getAll", summary = "Get All Users", description = "Get All Users", tags = {"user"},
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            responses =  {
                                    @ApiResponse(responseCode = "200", description = "successful operation",
                                            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized")
                            })
            ),

            @RouterOperation(
                    path = USER_END_POINT + "/{id}", produces = {APPLICATION_JSON_VALUE}, method = RequestMethod.GET,
                    operation = @Operation(operationId = "getUser", summary = "Get User by ID", description = "Get User by ID", tags = {"user"},
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "id") },
                            responses =  {
                                    @ApiResponse(responseCode = "200", description = "successful operation",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            })
            ),
            @RouterOperation(
                    path = USER_END_POINT, produces = {APPLICATION_JSON_VALUE}, method = RequestMethod.POST,
                    operation = @Operation(operationId = "saveUser", summary = "Add User", description = "Add new user to database", tags = {"user"},
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = User.class))),
                            responses =  {
                                    @ApiResponse(responseCode = "201", description = "Used saved successfully",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized")
                            })
            ),
            @RouterOperation(
                    path = USER_END_POINT + "/{id}", method = RequestMethod.PUT,
                    operation = @Operation(operationId = "updateUser", summary = "Update User", description = "Update User informations", tags = {"user"},
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "id") },
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = User.class))),
                            responses =  {
                                    @ApiResponse(responseCode = "201", description = "Used saved successfully",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            })
            ),
            @RouterOperation(
                    path = USER_END_POINT + "/{id}", method = RequestMethod.DELETE,
                    operation = @Operation(operationId = "deleteUser", summary = "Delete User", description = "Delete user from database", tags = {"user"},
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "id") },
                            responses =  {
                                    @ApiResponse(responseCode = "204", description = "No content"),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            })
            ),
    })

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route()
                .nest(path(USER_END_POINT), builder -> {
                    builder
                            .GET("", userHandler::getAll)
                            .GET("/{id}", userHandler::getUserById)
                            .POST("", userHandler::saveUser)
                            .PUT("/{id}", userHandler::updateUser)
                            .DELETE("/{id}", userHandler::deleteUser)
                            .GET("/stream", userHandler::getUsersStream);
                })
                //.GET("/v1/hello", (request ->  ServerResponse.ok().bodyValue("hello")))
                .build();
    }
}
