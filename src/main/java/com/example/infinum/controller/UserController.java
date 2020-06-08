package com.example.infinum.controller;

import com.example.infinum.domain.User;
import com.example.infinum.dto.UserDto;
import com.example.infinum.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
public class UserController  extends BaseController<UserDto, User, Long>{

    private UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    /**
     * Add favourite city in list of users most favourites cities
     * @param id
     * @param principal
     * @return UserDto
     */
    @PostMapping("city/{id}")
    public UserDto addFavourite(@PathVariable Long id, Principal principal) {
        return userService.addFavouriteCity(id, principal.getName());
    }

    /**
     * Removes favourite city form list of user favourites cities
     * @param id
     * @param principal
     * @return UserDto
     */
    @PutMapping("city/{id}")
    public UserDto removeFavourite(@PathVariable Long id, Principal principal) {
        return userService.removeFavouriteCity(id, principal.getName());
    }
}
