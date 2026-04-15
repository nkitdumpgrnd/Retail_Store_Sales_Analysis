package com.retailfsd.sales.controller;

import com.retailfsd.sales.dto.AuthRequest;
import com.retailfsd.sales.dto.AuthResponse;
import com.retailfsd.sales.repository.UserAccountRepository;
import com.retailfsd.sales.security.JwtService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
                          UserAccountRepository userAccountRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String role = userAccountRepository.findByUsername(user.getUsername())
                .map(account -> account.getRole())
                .orElse("ROLE_ANALYST");
        return ResponseEntity.ok(new AuthResponse(jwtService.generateToken(user), user.getUsername(), role));
    }
}
