package com.example.springJWT.service;

import com.example.springJWT.dto.CustomUserDetails;
import com.example.springJWT.entity.User_WC;
import com.example.springJWT.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User_WC user = userRepository.findByUsername(username);

        if(user != null){

            return new CustomUserDetails(user);
        }

        return null;
    }
}
