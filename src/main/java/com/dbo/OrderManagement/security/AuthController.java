package com.dbo.OrderManagement.security;

import com.dbo.OrderManagement.entity.User;
import com.dbo.OrderManagement.security.LoginRequest;
import com.dbo.OrderManagement.security.SignUpRequest;
import com.dbo.OrderManagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService){
        this.userService=userService;

    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        userService.signup(signUpRequest);

        return ResponseEntity.ok("User Registered Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        AuthResponse authResponse=userService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestParam String refreshtoken){
        String accessToken=userService.generateAccessToken(refreshtoken);

        return ResponseEntity.ok(accessToken);
    }
}
