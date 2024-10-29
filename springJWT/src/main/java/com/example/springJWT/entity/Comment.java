package com.example.springJWT.entity;

import com.example.springJWT.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String username;

    @Column
    private String content;

    public static Comment createComment(String username, CommentDto dto, Member member) {

        return new Comment(
                null,
                member,
                username,
                dto.getContent()
        );
    }

    public void patch(CommentDto dto) {

        if(dto.getContent() != null)
            this.content = dto.getContent();
    }
}
