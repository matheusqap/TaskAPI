package com.Token.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Token.models.User;
import com.Token.repositories.UserRepository;

@Service
public class UserService
{
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password)
    {
        if(userRepository.existsByUsername(username))
        {
            throw new RuntimeException("Username already exists");
        }
        String encryptedPassword = passwordEncoder.encode(password);
        User user = new User(username, encryptedPassword);
        return userRepository.save(user);
    }

    public User authenticate(String username, String password)
    {
        return userRepository.findByUsername(username)
        .filter(user -> passwordEncoder.matches(password, user.getPassword()))
        .orElse(null);
    }
     
}