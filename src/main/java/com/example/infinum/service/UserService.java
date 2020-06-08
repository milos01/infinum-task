package com.example.infinum.service;

import com.example.infinum.dto.UserDto;


public interface UserService extends BaseService<UserDto, Long>{
    boolean doesUserExists(String email);

    UserDto addFavouriteCity(Long id, String claim);

    UserDto removeFavouriteCity(Long id, String claim);
}
