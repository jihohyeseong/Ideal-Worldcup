package com.example.springJWT.service;

import com.example.springJWT.dto.MemberDto;
import com.example.springJWT.entity.Member;
import com.example.springJWT.entity.Worldcup;
import com.example.springJWT.repository.MemberRepository;
import com.example.springJWT.repository.WorldcupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorldcupRepository worldcupRepository;
    Long game = 0L;

    private final List<Long> excludedIds = new ArrayList<>();
    private final List<Long> winIds = new ArrayList<>();
    private final Random random = new Random();

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

    @Transactional
    public List<MemberDto> getTwoRandomMembers(Long id){

        if(game != (winIds.size())){
            excludedIds.remove(excludedIds.size() -1);
            excludedIds.remove(excludedIds.size() -1);
        }
        else {
            game = game + 1L;
        }

        // 데이터베이스에서 모든 멤버를 가져옴
        List<Member> allMembers = memberRepository.findAllByWorldcup_IdOrderByVictoryNumDesc(id);

        System.out.println(excludedIds);

        // 제외된 멤버를 필터링하여 가져옴
        List<Member> eligibleMembers = allMembers.stream()
                .filter(member -> !excludedIds.contains(member.getId()))
                .collect(Collectors.toList());

        if (eligibleMembers.size() == 2) {
            Member member1 = eligibleMembers.get(0);
            Member member2 = eligibleMembers.get(1);

            member1.setLoseNum(member1.getLoseNum() + 1L);
            member2.setLoseNum(member2.getLoseNum() + 1L);
            excludedIds.addAll(eligibleMembers.stream().map(Member::getId).toList());
            return eligibleMembers.stream()
                    .map(Member::createMemberDto)
                    .collect(Collectors.toList());
        }

        if (eligibleMembers.size() == 1) {

            excludedIds.addAll(eligibleMembers.stream().map(Member::getId).toList());
            return eligibleMembers.stream()
                    .map(Member::createMemberDto)
                    .collect(Collectors.toList());
        }

        // 유효한 멤버 중 두 개를 랜덤으로 선택
        List<MemberDto> selectedMembers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int randomIndex = random.nextInt(eligibleMembers.size());
            Member selectedMember = eligibleMembers.get(randomIndex);

            selectedMember.setLoseNum(selectedMember.getLoseNum() + 1L);
            memberRepository.save(selectedMember);

            // 선택된 멤버를 DTO로 변환하여 결과 목록에 추가
            selectedMembers.add(Member.createMemberDto(selectedMember));

            // 선택된 멤버의 ID를 제외 목록에 추가
            excludedIds.add(selectedMember.getId());

            // 선택된 멤버를 유효한 목록에서 제거
            eligibleMembers.remove(randomIndex);
        }

        return selectedMembers;
    }

    public void resetExcludedMembers(){

        excludedIds.clear();
        winIds.clear();
        game = 0L;
    }

    @Transactional
    public void goNextRound() {

        for(Long id : winIds){

            excludedIds.remove(id);
        }
        if(winIds.size() == 1){

            Long id = winIds.get(0);
            Member member = memberRepository.findById(id).orElse(null);
            member.setVictoryNum(member.getVictoryNum() + 1L);
            memberRepository.save(member);
        }
        winIds.clear();
        game = 0L;
    }

    @Transactional
    public void win(Long id) {

        winIds.add(id);
        Member member = memberRepository.findById(id).orElse(null);

        member.setWinNum(member.getWinNum() + 1L);
        member.setLoseNum(member.getLoseNum() - 1L);
        memberRepository.save(member);
    }

    public String saveImage(MultipartFile image) throws IOException {

        String folderPath = "C:/Spring/springJWT/src/main/resources/static/images/";  // 원하는 경로로 변경하세요
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

    public void clearLoseNum(Long id1, Long id2) {

        Member member1 = memberRepository.findById(id1).orElse(null);
        Member member2 = memberRepository.findById(id2).orElse(null);

        member1.setLoseNum(member1.getLoseNum() - 1L);
        member2.setLoseNum(member2.getLoseNum() - 1L);
    }
}
