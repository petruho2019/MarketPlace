package com.petruho.spring.project.marketplace.service;

import com.petruho.spring.project.marketplace.error.EmailNotFoundException;
import com.petruho.spring.project.marketplace.error.UsernameException;
import com.petruho.spring.project.marketplace.model.User;
import com.petruho.spring.project.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user){
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UsernameException("Username is used: " + user.getUsername());
        }
        else if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new EmailNotFoundException("Email is used: " + user.getEmail());
        }
        else
            return save(user);
    }

    public User getUserByUsername(String username) {

        return userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameException("Username not found: " + username));
    }

    public UserDetailsService userDetailsService() {
        return this::getUserByUsername;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user =  userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameException("Username not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}
