package com.example.springJWT.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.example.springJWT.config.S3Config;
import com.example.springJWT.dto.MemberDto;
import com.example.springJWT.entity.Member_WC;
import com.example.springJWT.entity.Worldcup_WC;
import com.example.springJWT.repository.MemberRepository;
import com.example.springJWT.repository.WorldcupRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorldcupRepository worldcupRepository;
    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final Map<String, UserGameState> gameStates = new HashMap<>();

    @Getter
    @Setter
    private static class UserGameState {
        private List<Long> winIds = new ArrayList<>();
        private Long game = 0L;
        private int index = 0;
        private List<Member_WC> members = new ArrayList<>();
    }


    public MemberService(MemberRepository memberRepository, WorldcupRepository worldcupRepository, S3Config s3Config){

        this.memberRepository = memberRepository;
        this.worldcupRepository = worldcupRepository;
        this.s3Config = s3Config;
    }

    @Transactional
    public List<MemberDto> getAllMembers(Long id) {

        List<Member_WC> members = memberRepository.findAllByWorldcup_IdOrderByVictoryNumDesc(id);
        List<MemberDto> memberDtoList = new ArrayList<>();

        for(Member_WC m : members){
            MemberDto dto = Member_WC.createMemberDto(m);
            memberDtoList.add(dto);
        }

        Worldcup_WC worldcup = worldcupRepository.findById(id).orElse(null);
        worldcup.setViewsCount(worldcup.getViewsCount() + 1L);
        worldcupRepository.save(worldcup);

        return memberDtoList;
    }

    public List<Member_WC> gameStart(Long id){

        // 데이터베이스에서 모든 멤버를 가져옴
        List<Member_WC> members = memberRepository.findAllByWorldcup_IdOrderByVictoryNumDesc(id);

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
            return gameStates.get(username).getMembers().stream().map(Member_WC::createMemberDto).collect(Collectors.toList());
        }

        List<Member_WC> selected = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            Member_WC member = gameStates.get(username).getMembers().get(gameStates.get(username).getIndex());
            selected.add(member);
            member.setLoseNum(member.getLoseNum() + 1);
            memberRepository.save(member);
            gameStates.get(username).setIndex(gameStates.get(username).getIndex() + 1);
        }

        return selected.stream().map(Member_WC::createMemberDto).collect(Collectors.toList());
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
            Member_WC member = memberRepository.findById(id).orElse(null);
            member.setVictoryNum(member.getVictoryNum() + 1L);
            memberRepository.save(member);
        }
        gameStates.get(username).setIndex(0);
        List<Member_WC> winMembers = memberRepository.findAllById(gameStates.get(username).getWinIds());
        Collections.shuffle(winMembers);
        gameStates.get(username).setMembers(winMembers);
        gameStates.get(username).winIds.clear();
    }

    @Transactional
    public void win(Long id, String username) {

        gameStates.get(username).getWinIds().add(id);
        System.out.println(gameStates.get(username).getWinIds());
        Member_WC member = memberRepository.findById(id).orElse(null);

        member.setWinNum(member.getWinNum() + 1L);
        memberRepository.save(member);
    }

//    public String saveImage(MultipartFile image) throws IOException {
//        String folderPath = "/app/uploads/"; // 도커 컨테이너 안 경로
//        String originalFilename = image.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자
//        String fileName = UUID.randomUUID() + extension; // 안전한 파일명 생성
//        Path imagePath = Paths.get(folderPath + fileName);
//
//        Files.createDirectories(Paths.get(folderPath)); // 폴더 없으면 생성
//        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING); // 저장
//
//        return "/uploads/" + fileName; // 프론트에 돌려줄 URL
//    }

    public String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuidFileName = UUID.randomUUID() + extension;

        // S3에 업로드
        s3Config.amazonS3Client().putObject(
                bucket,
                uuidFileName,
                image.getInputStream(),
                null
        );

        // 퍼블릭 읽기 권한 부여
        s3Config.amazonS3Client().setObjectAcl(bucket, uuidFileName, CannedAccessControlList.PublicRead);

        // 업로드된 이미지 URL 반환
        return s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();
    }

    public MemberDto createMember(Long worldcupId, MemberDto dto) {

        Member_WC member = MemberDto.toEntity(dto);
        Worldcup_WC worldcup = worldcupRepository.findById(worldcupId).orElse(null);
        member.setWorldcup(worldcup);

        Member_WC created = memberRepository.save(member);

        return Member_WC.createMemberDto(created);
    }

}