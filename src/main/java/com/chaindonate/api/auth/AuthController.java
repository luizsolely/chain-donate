package com.chaindonate.api.auth;

import com.chaindonate.api.auth.dto.*;
import com.chaindonate.api.dto.SimpleCampaignDTO;
import com.chaindonate.api.dto.UserResponseDTO;
import com.chaindonate.api.entity.User;
import com.chaindonate.api.exception.EmailAlreadyInUseException;
import com.chaindonate.api.exception.InvalidCredentialsException;
import com.chaindonate.api.repository.UserRepository;
import com.chaindonate.api.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        var user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(encoder.encode(request.password()));

        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use: " + request.email());
        }

        userRepo.save(user);

        var token = jwtService.generateToken(user.getEmail());

        List<SimpleCampaignDTO> campaignDTOs = user.getCampaigns().stream()
                .map(c -> new SimpleCampaignDTO(c.getId(), c.getTitle(), c.getBtcAddress()))
                .toList();

        return ResponseEntity.ok(new AuthResponse(token, new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), campaignDTOs)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            authManager.authenticate(authToken);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        var user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));


        var token = jwtService.generateToken(user.getEmail());

        List<SimpleCampaignDTO> campaignDTOs = user.getCampaigns().stream()
                .map(c -> new SimpleCampaignDTO(c.getId(), c.getTitle(), c.getBtcAddress()))
                .toList();

        return ResponseEntity.ok(new AuthResponse(token, new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), campaignDTOs)));
    }
}
