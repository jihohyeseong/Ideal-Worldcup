package com.example.springJWT.controller;

import com.example.springJWT.dto.JoinDto;
import com.example.springJWT.entity.User;
import com.example.springJWT.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinDto joinDto){
        User user = joinService.joinProcess(joinDto);
        return (user != null) ?
                ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
