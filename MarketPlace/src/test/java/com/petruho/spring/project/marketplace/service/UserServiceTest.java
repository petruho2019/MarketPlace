package com.petruho.spring.project.marketplace.service;


import com.petruho.spring.project.marketplace.error.EmailNotFoundException;
import com.petruho.spring.project.marketplace.error.UsernameException;
import com.petruho.spring.project.marketplace.model.Role;
import com.petruho.spring.project.marketplace.model.User;
import com.petruho.spring.project.marketplace.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("petruho");
        user.setEmail("petruho@mail.ru");
    }

    @Test
    public void createExistsByUsernameTest() {
        // Настраиваем поведение userRepository
        Mockito.when(userRepository.existsUserByUsername(user.getUsername())).thenReturn(true);

        Assertions.assertThrows(UsernameException.class, () -> {
            userService.create(user);
        });

        Mockito.verify(userRepository, Mockito.times(1)).existsUserByUsername(user.getUsername());
    }

    @Test
    public void createExistsByEmailTest() {
        // Настраиваем поведение userRepository
        Mockito.when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true);

        Assertions.assertThrows(EmailNotFoundException.class, () -> {
            userService.create(user);
        });

        Mockito.verify(userRepository, Mockito.times(1)).existsUserByEmail(user.getEmail());
    }

    @Test
    public void createToSave() {
        // Настраиваем поведение userRepository
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User savedUser = userService.create(user);
        Assertions.assertEquals("petruho", savedUser.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void getUserByUsernameExceptionTest() {

        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenThrow(UsernameException.class);

        Assertions.assertThrows(UsernameException.class, () -> {
            userService.getUserByUsername("petruho2019");
        });

        Mockito.verify(userRepository,Mockito.times(1)).findUserByUsername("petruho2019");
    }

    @Test
    public void getUserByUsernameTest() {
        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        Assertions.assertEquals("petruho", userService.getUserByUsername("petruho").getUsername());

        Mockito.verify(userRepository,Mockito.times(1)).findUserByUsername("petruho");
    }

    @Test
    public void loadUserByUsernameTest() throws IllegalAccessException {
        user.setPassword("petruho");
        Class<User> userClass = User.class;

        Field[] fields = userClass.getDeclaredFields();

        for (Field field : fields){
            field.setAccessible(true);
            if (field.getName().equals("role")){
                field.set(user,Role.USER);
                break;
            }
        }

        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        Assertions.assertEquals("petruho", userService.loadUserByUsername("petruho").getUsername());
    }

}
