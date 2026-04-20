package com.dbo.OrderManagement.security;

import com.dbo.OrderManagement.entity.User;
import com.dbo.OrderManagement.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public  CustomUserDetailService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
         User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
         return new org.springframework.security.core.userdetails.User(
             user.getUsername(),
             user.getPassword(),
                     List.of(new SimpleGrantedAuthority(user.getRole().name()))
         );

    }
}
