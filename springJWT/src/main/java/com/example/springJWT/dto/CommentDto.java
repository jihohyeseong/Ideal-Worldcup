package com.example.springJWT.dto;

import com.example.springJWT.entity.Comment;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentDto {

    private Long id;
    private Long memberId;
    private String username;
    private String content;

    public static CommentDto createCommentDto(Comment c) {
        return new CommentDto(
                c.getId(),
                c.getMember().getId(),
                c.getUsername(),
                c.getContent()
        );
    }
}
