package com.chaindonate.api.auth;

import com.chaindonate.api.auth.dto.*;
import com.chaindonate.api.dto.UserResponseDTO;
import com.chaindonate.api.entity.User;
import com.chaindonate.api.repository.UserRepository;
import com.chaindonate.api.auth.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepo, AuthenticationManager authManager, JwtService jwtService, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        var user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(encoder.encode(request.password()));
        userRepo.save(user);

        var token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail())));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authManager.authenticate(authToken);

        var user = userRepo.findByEmail(request.email()).orElseThrow();
        var token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail())));
    }
}
