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
public class Comment_WC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member_WC member;

    @Column
    private String username;

    @Column
    private String content;

    public static Comment_WC createComment(String username, CommentDto dto, Member_WC member) {

        return new Comment_WC(
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
