package com.example.springJWT.service;

import com.example.springJWT.dto.JoinDto;
import com.example.springJWT.entity.User;
import com.example.springJWT.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User joinProcess(JoinDto joinDto){

        boolean isExist = userRepository.existsByUsername(joinDto.getUsername());
        if(isExist){
            return null;
        }

        User user = JoinDto.toEntity(joinDto);
        user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
        return userRepository.save(user);
    }
}
