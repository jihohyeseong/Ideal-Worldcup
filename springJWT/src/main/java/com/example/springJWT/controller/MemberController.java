package com.example.springJWT.controller;

import com.example.springJWT.dto.CustomUserDetails;
import com.example.springJWT.dto.MemberDto;
import com.example.springJWT.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){

        this.memberService = memberService;
    }

    @GetMapping("/{id}/all")
    public ResponseEntity<List<MemberDto>> allMember(@PathVariable Long id){

        List<MemberDto> memberDtoList = memberService.getAllMembers(id);

        return ResponseEntity.status(HttpStatus.OK).body(memberDtoList);
    }

    @GetMapping("/{id}/random")
    public ResponseEntity<List<MemberDto>> getRandomMembers(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){

        String username = userDetails.getUsername();
        List<MemberDto> memberDtoList = memberService.getTwoRandomMembers(id, username);

        return ResponseEntity.status(HttpStatus.OK).body(memberDtoList);
    }

    @PostMapping("/reset") // excludedIds 초기화
    public void resetExcludedMembers(@AuthenticationPrincipal CustomUserDetails userDetails){

        String username = userDetails.getUsername();
        memberService.resetExcludedMembers(username);
    }

    @PostMapping("/select/{id}")
    public void win(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){

        String username = userDetails.getUsername();
        memberService.win(id, username);
    }

    @PostMapping("/next")
    public void goNextRound(@AuthenticationPrincipal CustomUserDetails userDetails){

        String username = userDetails.getUsername();
        memberService.goNextRound(username);
    }

    @PostMapping("/{worldcupId}")
    public ResponseEntity<MemberDto> makeMember(@PathVariable Long worldcupId,
                                                @RequestPart("data") MemberDto dto,
                                                @RequestPart("image") MultipartFile image) throws IOException {

        String imageUrl = memberService.saveImage(image);
        dto.setImageUrl(imageUrl);
        MemberDto memberDto = memberService.createMember(worldcupId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(memberDto);
    }
}
