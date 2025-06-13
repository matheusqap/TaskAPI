package com.Token.controllers;

import com.Token.models.User;
import com.Token.services.UserService;
import com.Token.utils.JwtUtil;
import com.Token.DTOs.LoginDTO;
import com.Token.DTOs.RegisterDTO;
import com.Token.DTOs.TokenResponseDTO;
import com.Token.DTOs.UserProfileDTO;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        try {
            User user = userService.registerUser(username, password, profilePicture);
            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao processar imagem");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO data) {
        try {
            User user = userService.authenticate(data.username, data.password);
            if (user == null) throw new RuntimeException("Invalid credentials");
            String token = jwtUtil.generateToken(user.getUsername());
            //TokenResponseDTO response = new TokenResponseDTO(token); -- descomentar caso queira resposta: {"token: "token" "}
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        try {
            UserProfileDTO profile = userService.getLoggedUserProfile();
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
