package com.example.infinum.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto extends Id<Long>{

    private String name;

    @NotBlank(message = "Email must be provided")
    @Email(message = "Not valid email address format")
    private String email;

    @NotBlank(message = "Password must be provided")
    @Size(
            min = 3,
            message = "The password '${validatedValue}' must be longer than {min} characters"
    )
    private String password;

    private List<CityDto> favouriteCities;
}
