package com.dm.vavr.crud.service;

import com.dm.vavr.crud.data.User;
import com.dm.vavr.crud.data.UserRepository;
import org.springframework.data.repository.CrudRepository;

public class UserService implements SpringDataCRUDService<User, String, UserDto> {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String entityName() {
        return "User";
    }

    @Override
    public User create(final UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword("");

        return userRepository.save(user);
    }

    @Override
    public User _update(final User user, final UserDto userDto) {

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        return userRepository.save(user);
    }

    @Override
    public CrudRepository<User, String> repository() {
        return userRepository;
    }
}
