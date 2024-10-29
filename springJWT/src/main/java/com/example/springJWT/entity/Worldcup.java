package com.example.springJWT.entity;

import com.example.springJWT.dto.WorldcupDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Worldcup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; // 월드컵 주제

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl; // 메인 사진 url

    @Column
    private String description; // 요약 설명

    @Column
    private LocalDateTime createdAt; // 생성일자

    @Column
    private LocalDateTime updatedAt; // 수정일자

    @Column
    private Long viewsCount;

    @Column
    private String category; // 카테고리

    @Column
    private String createdBy; // 작성자

    @Column
    private Long likesCount; // 좋아요 수

//    @OneToMany(mappedBy = "worldcup", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Member> members; // Worldcup과 연결된 사진들

    public static WorldcupDto createWorldcupDto(Worldcup w) {
        return new WorldcupDto(
                w.getId(),
                w.getName(),
                w.getImageUrl(),
                w.getDescription(),
                w.getCreatedAt(),
                w.getUpdatedAt(),
                w.getViewsCount(),
                w.getCategory(),
                w.getCreatedBy(),
                w.getLikesCount()
        );
    }
}
