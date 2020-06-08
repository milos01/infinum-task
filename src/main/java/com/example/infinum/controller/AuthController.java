package com.example.infinum.controller;

import com.example.infinum.dto.AccessToken;
import com.example.infinum.dto.UserDto;
import com.example.infinum.dto.request.LoginRequest;
import com.example.infinum.service.UserService;
import com.example.infinum.service.impl.InfinumAccountServiceImpl;
import com.example.infinum.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private InfinumAccountServiceImpl infinumAccountService;
    private UserService userService;
    private ModelMapper mapper;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                          InfinumAccountServiceImpl infinumAccountService, UserService userService, ModelMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.infinumAccountService = infinumAccountService;
        this.userService = userService;
        this.mapper = mapper;
    }

    /**
     * Signin user with email and password
     * @param loginRequest
     * @return AccessToken
     */
    @PostMapping("signin")
    public AccessToken signin(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        authenticate(email, password);
        final UserDetails userDetails = infinumAccountService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());
        return new AccessToken(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    /**
     * Signup users
     * @param registerRequest
     * @return UserDto
     */
    @PostMapping("signup")
    public UserDto signup(@RequestBody @Valid UserDto registerRequest) {
        if(userService.doesUserExists(registerRequest.getEmail())) {
            throw new BadCredentialsException("This email already exists");
        }

        UserDto mappedDto = this.mapper.map(registerRequest, UserDto.class);
        return userService.save(mappedDto);
    }
}
