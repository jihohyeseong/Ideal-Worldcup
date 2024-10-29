package com.example.springJWT.repository;

import com.example.springJWT.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByWorldcup_IdOrderByVictoryNumDesc(Long id);



}
