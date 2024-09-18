package com.petruho.spring.project.marketplace.service;


import com.petruho.spring.project.marketplace.model.Role;
import com.petruho.spring.project.marketplace.model.User;
import com.petruho.spring.project.marketplace.request.SignInRequest;
import com.petruho.spring.project.marketplace.request.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder ;

    private SignInRequest signInRequest;

    private SignUpRequest signUpRequest;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp(){
        signInRequest = new SignInRequest();
        signInRequest.setUsername("petruho");
        signInRequest.setPassword("petruho");

        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("petruho");
        signUpRequest.setEmail("petruho@mail.ru");
        signUpRequest.setPassword("petruho");
    }

    @Test
    public void signInSuccessTest() {
        // Мокаем успешную аутентификацию
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(request.getSession(true)).thenReturn(session);

        // Вызываем метод
        authenticationService.signIn(signInRequest, request);

        // Проверяем, что аутентификация прошла
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(session, times(1)).setAttribute(
                eq(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY),
                eq(SecurityContextHolder.getContext())
        );
    }

    @Test
    public void signInFailureTest() {
        // Мокаем неуспешную аутентификацию
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect username or password"));

        // Проверяем, что выбрасывается исключение
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            authenticationService.signIn(signInRequest, request);
        });

        // Проверяем, что сессия не устанавливалась
        verify(session, never()).setAttribute(
                eq(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY),
                any()
        );
    }

    @Test
    public void signUpSuccessTest(){
        Mockito.when(userService.create(Mockito.any(User.class))).thenReturn(null);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        authenticationService.signUp(signUpRequest);

        verify(passwordEncoder, times(1)).encode(signUpRequest.getPassword());

        verify(userService, times(1)).create(Mockito.argThat((User user) ->
                user.getUsername().equals(signUpRequest.getUsername()) &&
                        user.getEmail().equals(signUpRequest.getEmail()) &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getRole().equals(Role.USER)
        ));
    }
}
