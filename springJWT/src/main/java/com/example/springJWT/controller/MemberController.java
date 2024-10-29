package com.example.springJWT.controller;

import com.example.springJWT.dto.MemberDto;
import com.example.springJWT.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<MemberDto>> getRandomMembers(@PathVariable Long id){

        List<MemberDto> memberDtoList = memberService.getTwoRandomMembers(id);

        return ResponseEntity.status(HttpStatus.OK).body(memberDtoList);
    }

    @PostMapping("/reset") // excludedIds 초기화
    public void resetExcludedMembers(){

        memberService.resetExcludedMembers();
    }

    @PostMapping("/select/{id}")
    public void win(@PathVariable Long id){

        memberService.win(id);
    }

    @PostMapping("/next")
    public void goNextRound(){

        memberService.goNextRound();
    }

    @PostMapping("/clear/{id1}/{id2}")
    public void clearLoseNum(@PathVariable Long id1, @PathVariable Long id2){

        memberService.clearLoseNum(id1, id2);
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
