package com.example.platforme.controllers;

import com.example.platforme.dtos.LoginRequestDTO;
import com.example.platforme.models.Role;
import com.example.platforme.models.Utilisateur;
import com.example.platforme.repositories.UtilisateurRepository;
import com.example.platforme.services.CustomUserDetailsService;
import com.example.platforme.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          UserDetailsService userDetailsService,
                          UtilisateurRepository utilisateurRepository,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/test-register")
    public ResponseEntity<?> createTestUser() {
        if (utilisateurRepository.findByEmail("test@demo.com").isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur existe déjà !");
        }

        Utilisateur user = new Utilisateur();
        user.setNom("Test");
        user.setPrenom("User");
        user.setEmail("test@demo.com");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(Role.CANDIDAT);

        utilisateurRepository.save(user);
        return ResponseEntity.ok("Utilisateur 'test@demo.com' créé avec mot de passe '123456'");
    }
}



