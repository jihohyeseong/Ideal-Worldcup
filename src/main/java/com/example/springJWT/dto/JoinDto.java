package com.example.springJWT.dto;

import com.example.springJWT.entity.User_WC;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDto {

    private String username;
    private String password;

    public static User_WC toEntity(JoinDto joinDto) {

        return new User_WC(null, joinDto.username, null, "ROLE_USER");
    }
}
