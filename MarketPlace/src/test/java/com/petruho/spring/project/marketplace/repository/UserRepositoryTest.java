package com.petruho.spring.project.marketplace.repository;


import com.petruho.spring.project.marketplace.model.Role;
import com.petruho.spring.project.marketplace.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void findUserByUsernameSuccessTest(){
        User user = User.builder()
                .id(2)
                .username("petruho")
                .password(passwordEncoder.encode("password"))
                .email("petruho@mail.ru")
                .role(Role.USER)
                .build();

        User userInDB = userRepository.findUserByUsername("petruho").orElse(null);

        Assertions.assertNotNull(userInDB);

        Assertions.assertEquals(user.getUsername(), userInDB.getUsername());
        Assertions.assertEquals(user.getEmail(), userInDB.getEmail());
        Assertions.assertEquals(user.getRole(), userInDB.getRole());
    }


    @Test
    public void existsUserByUsernameSuccessTest(){
        String username = "petruho";

        Assertions.assertTrue(userRepository.existsUserByUsername(username));
    }

    @Test
    public void existsUserByUsernameFailureTest(){
        String username = "bob";

        Assertions.assertFalse(userRepository.existsUserByUsername(username));
    }

    @Test
    public void existsUserByEmailSuccessTest(){
        String email = "petruho@mail.ru";

        Assertions.assertTrue(userRepository.existsUserByEmail(email));
    }

    @Test
    public void existsUserByEmailFailureTest(){
        String email = "bob@mail.ru";

        Assertions.assertFalse(userRepository.existsUserByUsername(email));
    }
}
