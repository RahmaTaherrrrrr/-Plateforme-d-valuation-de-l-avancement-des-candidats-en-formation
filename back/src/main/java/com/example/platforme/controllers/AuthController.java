package com.example.platforme.controllers;

import com.example.platforme.dtos.JwtAuthenticationResponse;
import com.example.platforme.dtos.LoginRequestDTO;
import com.example.platforme.dtos.UtilisateurCreateDTO;
import com.example.platforme.dtos.UtilisateurDTO;
import com.example.platforme.mappers.UtilisateurMapper;
import com.example.platforme.models.Utilisateur;
import com.example.platforme.services.JwtService;
import com.example.platforme.services.UserInfoDetails;
import com.example.platforme.services.UtilisateurServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth" )
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurServiceImpl utilisateurService;
    private final UtilisateurMapper utilisateurMapper;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UtilisateurServiceImpl utilisateurService,
                          UtilisateurMapper utilisateurMapper,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.utilisateurService = utilisateurService;
        this.utilisateurMapper = utilisateurMapper;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userInfoDetails.getUtilisateur();

        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDTO(utilisateur);
        String token = jwtService.generateToken(userInfoDetails); // On peut passer userInfoDetails directement

        JwtAuthenticationResponse response = new JwtAuthenticationResponse(token, utilisateurDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UtilisateurDTO> register(@Valid @RequestBody UtilisateurCreateDTO request) {
        UtilisateurDTO createdUser = utilisateurService.create(request);
        return ResponseEntity.ok(createdUser);
    }
}
