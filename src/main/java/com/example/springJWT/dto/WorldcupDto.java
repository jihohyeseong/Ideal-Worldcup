package com.example.springJWT.dto;

import com.example.springJWT.entity.Worldcup_WC;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorldcupDto {

    private Long id;
    private String name; // 월드컵 주제
    private String imageUrl; // 메인 사진 url
    private String description; // 요약 설명
    private LocalDateTime createdAt; // 생성일자
    private LocalDateTime updatedAt; // 수정일자
    private Long viewsCount; // 조회수
    private String category; // 카테고리
    private String createdBy; // 작성자
    private Long likesCount; // 좋아요 수

    public static Worldcup_WC createWorldcup(WorldcupDto dto, String username) {

        return new Worldcup_WC(
                null,
                dto.getName(),
                dto.getImageUrl(),
                dto.getDescription(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                dto.getCategory(),
                username,
                0L
                );
    }
}
