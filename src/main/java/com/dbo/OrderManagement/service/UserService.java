package com.dbo.OrderManagement.service;

import com.dbo.OrderManagement.security.AuthResponse;
import com.dbo.OrderManagement.security.JwtUtil;
import com.dbo.OrderManagement.security.LoginRequest;
import com.dbo.OrderManagement.security.SignUpRequest;
import com.dbo.OrderManagement.entity.User;
import com.dbo.OrderManagement.enums.Role;
import com.dbo.OrderManagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public void signup(SignUpRequest signUpRequest){

        if(userRepository.findByUsername(signUpRequest.getUsername()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        User user=new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);

        //createAdminAccount();

        userRepository.save(user);


    }

    public AuthResponse login(LoginRequest loginRequest){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user=userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()->new RuntimeException("User Not found"));

//        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
//            throw new RuntimeException("Password is incorrect");
//        }

        String accesstoken = jwtUtil.generateToken(user.getUsername(),"ROLE_"+user.getRole().name());
        String refreshToken= jwtUtil.generateRefreshToken(user.getUsername());

        AuthResponse authResponse=new AuthResponse();
        authResponse.setAccessToken(accesstoken);
        authResponse.setRefreshToken(refreshToken);

       return authResponse;
    }


    public String generateAccessToken(String refreshtoken){
        String username=jwtUtil.extractUserName(refreshtoken);

        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User Not found"));

        return jwtUtil.generateToken(user.getUsername(),user.getRole().name());
    }

    public void createAdminAccount(){
        if(userRepository.findByUsername("Admin").isEmpty()){

            User admin=new User();
            admin.setUsername("Admin");
            admin.setPassword(passwordEncoder.encode("Admin123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }
    }



}
