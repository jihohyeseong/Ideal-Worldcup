package com.example.springJWT.repository;

import com.example.springJWT.entity.Member_WC;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface MemberRepository extends JpaRepository<Member_WC, Long> {

    List<Member_WC> findAllByWorldcup_IdOrderByVictoryNumDesc(Long id);


}
