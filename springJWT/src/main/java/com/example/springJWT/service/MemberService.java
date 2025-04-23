package com.example.springJWT.service;

import com.example.springJWT.dto.MemberDto;
import com.example.springJWT.entity.Member;
import com.example.springJWT.entity.Worldcup;
import com.example.springJWT.repository.MemberRepository;
import com.example.springJWT.repository.WorldcupRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorldcupRepository worldcupRepository;
    private final Map<String, UserGameState> gameStates = new HashMap<>();

    @Getter
    @Setter
    private static class UserGameState {
        private List<Long> winIds = new ArrayList<>();
        private Long game = 0L;
        private int index = 0;
        private List<Member> members = new ArrayList<>();
    }


    public MemberService(MemberRepository memberRepository, WorldcupRepository worldcupRepository){

        this.memberRepository = memberRepository;
        this.worldcupRepository = worldcupRepository;
    }

    @Transactional
    public List<MemberDto> getAllMembers(Long id) {

        List<Member> members = memberRepository.findAllByWorldcup_IdOrderByVictoryNumDesc(id);
        List<MemberDto> memberDtoList = new ArrayList<>();

        for(Member m : members){
            MemberDto dto = Member.createMemberDto(m);
            memberDtoList.add(dto);
        }

        Worldcup worldcup = worldcupRepository.findById(id).orElse(null);
        worldcup.setViewsCount(worldcup.getViewsCount() + 1L);
        worldcupRepository.save(worldcup);

        return memberDtoList;
    }

    public List<Member> gameStart(Long id){

        // 데이터베이스에서 모든 멤버를 가져옴
        List<Member> members = memberRepository.findAllByWorldcup_IdOrderByVictoryNumDesc(id);

        if(members.size() < 2)
            return Collections.emptyList();
        else if(members.size() < 4) {
            Collections.shuffle(members);
            members = members.subList(0, 2);
        }
        else if(members.size() < 8) {
            Collections.shuffle(members);
            members = members.subList(0, 4);
        }
        else if(members.size() < 16) {
            Collections.shuffle(members);
            members = members.subList(0, 8);
        }
        else if(members.size() < 32) {
            Collections.shuffle(members);
            members = members.subList(0, 16);
        }
        else{
            Collections.shuffle(members);
            members = members.subList(0, 32);
        }

        return members;
    }

    @Transactional
    public List<MemberDto> getTwoRandomMembers(Long id, String username){

        gameStates.putIfAbsent(username, new UserGameState());

        if(gameStates.get(username).getGame() == 0) {
            gameStates.get(username).setMembers(gameStart(id));
            gameStates.get(username).setGame(1L);
        }

        if(gameStates.get(username).getIndex() == gameStates.get(username).getMembers().size())
            return Collections.emptyList();

        if(gameStates.get(username).getMembers().size() == 1) {
            return gameStates.get(username).getMembers().stream().map(Member::createMemberDto).collect(Collectors.toList());
        }

        List<Member> selected = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            Member member = gameStates.get(username).getMembers().get(gameStates.get(username).getIndex());
            selected.add(member);
            member.setLoseNum(member.getLoseNum() + 1);
            memberRepository.save(member);
            gameStates.get(username).setIndex(gameStates.get(username).getIndex() + 1);
        }

        return selected.stream().map(Member::createMemberDto).collect(Collectors.toList());
    }

    public void resetExcludedMembers(String username){

        if(gameStates.get(username) != null) {
            gameStates.get(username).getWinIds().clear();
            gameStates.get(username).setIndex(0);
            gameStates.get(username).setGame(0L);
            gameStates.get(username).getMembers().clear();
        }
    }

    @Transactional
    public void goNextRound(String username) {

        if(gameStates.get(username).getWinIds().size() == 1){

            Long id = gameStates.get(username).getWinIds().get(0);
            Member member = memberRepository.findById(id).orElse(null);
            member.setVictoryNum(member.getVictoryNum() + 1L);
            memberRepository.save(member);
        }
        gameStates.get(username).setIndex(0);
        List<Member> winMembers = memberRepository.findAllById(gameStates.get(username).getWinIds());
        Collections.shuffle(winMembers);
        gameStates.get(username).setMembers(winMembers);
        gameStates.get(username).winIds.clear();
    }

    @Transactional
    public void win(Long id, String username) {

        gameStates.get(username).getWinIds().add(id);
        System.out.println(gameStates.get(username).getWinIds());
        Member member = memberRepository.findById(id).orElse(null);

        member.setWinNum(member.getWinNum() + 1L);
        memberRepository.save(member);
    }

    public String saveImage(MultipartFile image) throws IOException {

        String folderPath = "C:/Spring/upload/";  // 원하는 경로로 변경하세요
        String fileName = image.getOriginalFilename();
        Path imagePath = Paths.get(folderPath + fileName);

        // 이미지 파일을 저장
        Files.copy(image.getInputStream(), imagePath);

        // 이미지 URL을 반환
        return  "/images/" + fileName;
    }

    public MemberDto createMember(Long worldcupId, MemberDto dto) {

        Member member = MemberDto.toEntity(dto);
        Worldcup worldcup = worldcupRepository.findById(worldcupId).orElse(null);
        member.setWorldcup(worldcup);

        Member created = memberRepository.save(member);

        return Member.createMemberDto(created);
    }

}
