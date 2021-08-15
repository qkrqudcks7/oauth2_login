package com.example.oauth2_login.controller;

import com.example.oauth2_login.exception.BadRequestException;
import com.example.oauth2_login.model.AuthProvider;
import com.example.oauth2_login.model.User;
import com.example.oauth2_login.payload.ApiResponse;
import com.example.oauth2_login.payload.AuthResponse;
import com.example.oauth2_login.payload.LoginRequest;
import com.example.oauth2_login.payload.SignUpRequest;
import com.example.oauth2_login.repository.UserRepository;
import com.example.oauth2_login.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenProvider.createToken(authenticate);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("이미 사용중인 이메일 입니다.");
        }

        User result = userRepository.save(User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .provider(AuthProvider.local)
                .build());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "회원가입 성공!"));
    }

    @PostMapping("/oauth2signup/{email}")
    public String registerOauth2User(@PathVariable("email") String email,
                                             @Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userRepository.findByEmail(email).get();
        if (user.getPassword().equals(null)) {
            throw new BadRequestException("이미 회원가입 된 이메일 입니다.");
        }

        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);

        return "회원가입 성공";
    }
}
