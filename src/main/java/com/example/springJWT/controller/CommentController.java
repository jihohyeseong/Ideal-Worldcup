package com.example.springJWT.controller;

import com.example.springJWT.dto.CommentDto;
import com.example.springJWT.dto.CustomUserDetails;
import com.example.springJWT.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){

        this.commentService = commentService;
    }

    @GetMapping("/{memberId}/all")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long memberId){
        List<CommentDto> dtos = commentService.allComments(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<CommentDto> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long memberId, @RequestBody CommentDto dto){
        String username = userDetails.getUsername();

        CommentDto createdDto = commentService.createComment(username, memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(createdDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentDto> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long id, @RequestBody CommentDto dto){
        String username = userDetails.getUsername();
        CommentDto updated = commentService.updateComment(username, id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDto> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long id){
        String username = userDetails.getUsername();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        CommentDto deleted;
        if (isAdmin) {
            // 관리자는 모든 댓글을 삭제할 수 있음
            deleted = commentService.deleteAsAdmin(id);
        } else {
            // 일반 사용자는 자신의 댓글만 삭제할 수 있음
            deleted = commentService.deleteComment(username, id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

}
