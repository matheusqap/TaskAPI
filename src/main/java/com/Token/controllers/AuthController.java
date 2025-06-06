package com.Token.controllers;

import com.Token.models.User;
import com.Token.services.UserService;
import com.Token.utils.JwtUtil;
import com.Token.DTOs.LoginDTO;
import com.Token.DTOs.RegisterDTO;
import com.Token.DTOs.TokenResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO data) {
        try {
            User user = userService.registerUser(data.username, data.password);
            return ResponseEntity.ok("User registered with ID: " + user.getId() + " Response Status: " + HttpStatus.OK.value());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

}
