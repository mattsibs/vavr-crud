package com.dm.vavr.crud.config;

import com.dm.vavr.crud.controller.UserController;
import com.dm.vavr.crud.data.UserRepository;
import com.dm.vavr.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CRUDConfiguration {

    private final UserRepository userRepository;

    @Autowired
    public CRUDConfiguration(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository);
    }

    @Bean
    public UserController userController() {
        return new UserController(userService());
    }
}
