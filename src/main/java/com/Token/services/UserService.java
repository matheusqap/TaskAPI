package com.Token.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Token.DTOs.UserProfileDTO;
import com.Token.models.User;
import com.Token.repositories.UserRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class UserService
{
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, MultipartFile profilePicture) throws IOException, java.io.IOException
    {
        if(userRepository.existsByUsername(username))
        {
            throw new RuntimeException("Username already exists");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User(username, encryptedPassword);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            user.setImage(profilePicture.getBytes());
        }

        return userRepository.save(user);
    }


    public User authenticate(String username, String password)
    {
        return userRepository.findByUsername(username)
        .filter(user -> passwordEncoder.matches(password, user.getPassword()))
        .orElse(null);
    }

    public UserProfileDTO getLoggedUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // mesmo que foi salvo no token
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UserProfileDTO(user.getUsername(), user.getImage());
    }
}