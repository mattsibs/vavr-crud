package com.dm.vavr.crud.service;

import com.dm.vavr.crud.data.User;
import com.dm.vavr.crud.data.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @InjectMocks
    private UserService userService;

    @Test
    public void repository_returnsRepository() throws Exception {
        assertThat(userService.repository())
                .isSameAs(userRepository);
    }

    @Test
    public void create_returnsNewUserWithDtoParams() throws Exception {
        UserDto userDto = new UserDto("username", "email");

        when(userRepository.save(any()))
                .then(invocationOnMock -> invocationOnMock.getArguments()[0]);

        User createdUser = userService.create(userDto);
        assertThat(createdUser)
                .extracting(User::getEmail, User::getUsername)
                .containsExactly("email", "username");
    }

    @Test
    public void create_callsSaveOnRepositoryWithNewUser() throws Exception {
        UserDto userDto = new UserDto("username", "email");

        userService.create(userDto);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue())
                .extracting(User::getEmail, User::getUsername)
                .containsExactly("email", "username");
    }

    @Test
    public void update_returnsNewUserWithDtoParams() throws Exception {
        UserDto userDto = new UserDto("username", "email");

        when(userRepository.save(any()))
                .then(invocationOnMock -> invocationOnMock.getArguments()[0]);

        User updatedUser = userService._update(new User(), userDto);

        assertThat(updatedUser)
                .extracting(User::getEmail, User::getUsername)
                .containsExactly("email", "username");
    }

    @Test
    public void update_callsSaveOnRepositoryWithNewUser() throws Exception {
        UserDto userDto = new UserDto("username", "email");

        userService._update(new User(), userDto);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue())
                .extracting(User::getEmail, User::getUsername)
                .containsExactly("email", "username");
    }
}