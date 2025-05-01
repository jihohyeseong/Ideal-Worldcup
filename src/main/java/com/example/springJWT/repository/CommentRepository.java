package com.example.springJWT.repository;

import com.example.springJWT.entity.Comment_WC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment_WC, Long> {

    List<Comment_WC> findByMemberId(Long memberId);
}
