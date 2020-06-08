package com.example.infinum.service.impl;

import com.example.infinum.domain.User;
import com.example.infinum.exceptions.NotFoundException;
import com.example.infinum.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class InfinumAccountServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public InfinumAccountServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Overriding method form UserDetail service that loads user from my datasource and by my criteria
     * @param email
     * @return UserDetails
     * @throws NotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new NotFoundException(User.class, "email", email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
