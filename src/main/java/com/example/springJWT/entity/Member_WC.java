package com.example.springJWT.entity;

import com.example.springJWT.dto.MemberDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member_WC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; // 이름

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl; // 이미지 주소

    @Column
    private Long winNum; // 이긴 횟수

    @Column
    private Long loseNum; // 등장 횟수

    @Column
    private Long victoryNum; // 우승 횟수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worldcup_id", nullable = false)
    private Worldcup_WC worldcup;

    public static MemberDto createMemberDto(Member_WC m) {

        return new MemberDto(
                m.getId(),
                m.getName(),
                m.getImageUrl(),
                m.getWinNum(),
                m.getLoseNum(),
                m.getVictoryNum()
        );
    }
}
