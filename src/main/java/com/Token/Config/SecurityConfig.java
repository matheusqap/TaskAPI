package com.Token.Config;

import com.Token.utils.JwtUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Desabilita CSRF (caso você não precise)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()  // Permite sem autenticação
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()  // Exige autenticação para qualquer outra requisição
            )
            .headers(headers -> 
            headers.frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);  // Adiciona o filtro JWT antes do filtro de autenticação padrão

        return http.build();
    }
}
