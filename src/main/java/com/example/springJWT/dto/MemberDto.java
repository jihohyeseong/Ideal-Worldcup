package com.example.springJWT.dto;

import com.example.springJWT.entity.Member_WC;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberDto {

    private Long id;
    private String name; // 이름
    private String imageUrl; // 이미지 주소
    private Long winNum; // 이긴 횟수
    private Long loseNum; // 진 횟수
    private Long victoryNum; // 우승 횟수

    public static Member_WC toEntity(MemberDto dto) {

        return new Member_WC(
                null,
                dto.getName(),
                dto.getImageUrl(),
                0L,
                0L,
                0L,
                null
        );
    }
}
