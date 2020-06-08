package com.example.infinum.service.impl;

import com.example.infinum.domain.City;
import com.example.infinum.domain.User;
import com.example.infinum.dto.UserDto;
import com.example.infinum.exceptions.NotFoundException;
import com.example.infinum.repository.CityRepository;
import com.example.infinum.repository.UserRepository;
import com.example.infinum.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<UserDto, User, Long> implements UserService {

    private UserRepository userRepository;
    private CityRepository cityRepository;

    public UserServiceImpl(UserRepository userRepository, CityRepository repository, ModelMapper mapper) {
        super(userRepository, mapper);
        this.userRepository = userRepository;
        this.cityRepository = repository;
    }


    /**
     * Check if user exist by email
     * @param email
     * @return boolean
     */
    @Override
    public boolean doesUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Add favourite city in list of users most favourites cities
     * @param id
     * @param claim
     * @return UserDtp
     */
    @Override
    public UserDto addFavouriteCity(Long id, String claim) {
        User user = this.getPrincipal(claim);
        City city = cityRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new NotFoundException(City.class, "id", id));
        if (!user.getFavourites().contains(city)) {
            user.getFavourites().add(city);
        }
        return this.mapper.map(user, UserDto.class);
    }

    /**
     * Removes favourite city form list of user favourites cities
     * @param id
     * @param claim
     * @return UserDto
     */
    @Override
    public UserDto removeFavouriteCity(Long id, String claim) {
        User user = this.getPrincipal(claim);
        City city = cityRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new NotFoundException(City.class, "id", id));
        user.getFavourites().remove(city);
        return this.mapper.map(user, UserDto.class);
    }

    /**
     * Get principal and maps it to the User object
     * @param claim
     * @return User
     */
    User getPrincipal(String claim) {
        return userRepository.findByEmail(claim).orElseThrow(() -> new NotFoundException(User.class, "email", claim));
    }
}
