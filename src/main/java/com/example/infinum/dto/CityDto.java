package com.example.infinum.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto extends Id<Long>{
    @NotBlank(message = "City name must be provided")
    private String name;

    @NotBlank(message = "City description must be provided")
    private String description;

    @Min(value = 1, message = "City population must be provided")
    private int population;
}