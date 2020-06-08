package com.example.infinum.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cities")
public class City extends BaseEntity<Long> {

    @NotNull
    private String name;

    @NotNull
    @Lob
    private String description;

    @NotNull
    private int population;

    @ManyToMany(mappedBy = "favourites")
    private List<User> user;
}
