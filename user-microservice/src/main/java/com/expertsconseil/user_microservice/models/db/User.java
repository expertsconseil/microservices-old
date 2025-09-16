package com.expertsconseil.user_microservice.models.db;

import com.expertsconseil.user_microservice.config.security.CustumUserDetails;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @NotNull(message = "User.fistName must be present")
    private String firstName;
    @NotBlank(message = "User.lastName must be present")
    private String lastName;
    @NotBlank(message = "User.email must be present")
    @Indexed(unique = true)
    private String email;
    private String address;
    //@Positive(message = "Birth day ")
    private LocalDate birthday;
    @Indexed
    private String username;
    private String password;

    @Field(name = "user_role")
    private String role;

    @Field(name = "account_enabled")
    private Boolean accountEnabled;

    @Field(name ="account_expired")
    private Boolean accountExpired;

    @Field(name ="account_locked")
    private Boolean accountLocked;

    @Field(name ="credentials_expired")
    private Boolean credentialsExpired;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public UserDetails toCustumUserDetails() {
        return new CustumUserDetails(
                username, password, role, accountExpired, accountLocked,
                credentialsExpired, accountEnabled, email, firstName, lastName);
    }


}
