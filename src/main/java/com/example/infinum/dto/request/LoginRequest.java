package com.example.infinum.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email must be provided")
    @Email(message = "Not valid email address format")
    private String email;

    @NotBlank(message = "Password must be provided")
    @Size(
            min = 3,
            message = "The password '${validatedValue}' must be longer than {min} characters"
    )
    private String password;
}

