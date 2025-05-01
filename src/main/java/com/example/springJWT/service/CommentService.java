package com.example.springJWT.service;

import com.example.springJWT.dto.CommentDto;
import com.example.springJWT.entity.Comment_WC;
import com.example.springJWT.entity.Member_WC;
import com.example.springJWT.repository.CommentRepository;
import com.example.springJWT.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository){

        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    public List<CommentDto> allComments(Long memberId) {

        List<Comment_WC> comments = commentRepository.findByMemberId(memberId);
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment_WC c : comments) {
            CommentDto dto = CommentDto.createCommentDto(c);
            dtos.add(dto);
        }
        return dtos;
    }


    public CommentDto createComment(String username, Long memberId, CommentDto dto) {

        Member_WC member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패."));
        Comment_WC comment = Comment_WC.createComment(username, dto, member);
        Comment_WC created = commentRepository.save(comment);

        return CommentDto.createCommentDto(created);
    }

    public CommentDto updateComment(String username, Long id, CommentDto dto) {

        Comment_WC target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패. 대상 댓글이 없음"));
        if (!target.getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글 수정 실패. 대상 댓글의 작성자가 아니고 권한이 없음");
        }
        target.patch(dto);
        Comment_WC updated = commentRepository.save(target);
        return CommentDto.createCommentDto(updated);
    }

    public CommentDto deleteAsAdmin(Long id) {

        Comment_WC target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패. 대상이 없습니다"));
        commentRepository.delete(target);
        return CommentDto.createCommentDto(target);
    }

    public CommentDto deleteComment(String username, Long id) {

        Comment_WC target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패. 대상이 없습니다"));
        if (!target.getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글 삭제 실패. 대상 댓글의 작성자가 아니고 권한이 없음");
        }
        commentRepository.delete(target);
        return CommentDto.createCommentDto(target);
    }
}
