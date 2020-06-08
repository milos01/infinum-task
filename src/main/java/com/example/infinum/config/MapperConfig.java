package com.example.infinum.config;

import com.example.infinum.domain.User;
import com.example.infinum.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MapperConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        modelMapper.createTypeMap(UserDto.class, User.class)
                .addMappings(m -> m.using(ctx -> passwordEncoder.encode((String) ctx.getSource())).map(UserDto::getPassword, User::setPassword));

        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMapping(User::getFavourites, UserDto::setFavouriteCities)
                .addMappings(m -> m.skip(UserDto::setPassword));

        return modelMapper;
    }
}
