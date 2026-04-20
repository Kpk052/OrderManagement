package com.dbo.OrderManagement.security;

import com.dbo.OrderManagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public JwtFilter(UserRepository userRepository,JwtUtil jwtUtil){
        this.userRepository=userRepository;
        this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {


        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

            String header=httpServletRequest.getHeader("Authorization");
            String path = httpServletRequest.getRequestURI();


            if (path.startsWith("/auth")) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            if(header==null || !header.startsWith("Bearer ")) {
               // httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }




                String token=header.substring(7);
                String username=jwtUtil.extractUserName(token);
                String role=jwtUtil.extractUserRole(token);

                if(username==null || userRepository.findByUsername(username).isEmpty()){
                   //httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                   return;
                }

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority(role)));

                SecurityContextHolder.getContext().setAuthentication(auth);

               filterChain.doFilter(httpServletRequest,httpServletResponse);



    }

}
