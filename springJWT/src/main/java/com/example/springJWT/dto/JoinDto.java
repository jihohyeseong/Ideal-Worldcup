package com.example.springJWT.dto;

import com.example.springJWT.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDto {

    private String username;
    private String password;

    public static User toEntity(JoinDto joinDto) {

        return new User(null, joinDto.username, null, "ROLE_USER");
    }
}
