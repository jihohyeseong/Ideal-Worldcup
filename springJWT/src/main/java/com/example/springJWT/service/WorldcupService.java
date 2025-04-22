package com.example.springJWT.service;

import com.example.springJWT.dto.WorldcupDto;
import com.example.springJWT.entity.Worldcup;
import com.example.springJWT.repository.WorldcupRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorldcupService {

    private final WorldcupRepository worldcupRepository;

    public WorldcupService(WorldcupRepository worldcupRepository){

        this.worldcupRepository = worldcupRepository;
    }

    public List<WorldcupDto> getWorldCupIndex() {

        List<Worldcup> worldcupList = worldcupRepository.findAll();
        List<WorldcupDto> worldcupDtoList = new ArrayList<>();
        for(Worldcup w : worldcupList){
            WorldcupDto dto = Worldcup.createWorldcupDto(w);
            worldcupDtoList.add(dto);
        }
        return worldcupDtoList;
    }

    public void likesCountUp(Long id) {

        Worldcup worldcup = worldcupRepository.findById(id).orElse(null);
        worldcup.setLikesCount(worldcup.getLikesCount() + 1L);
        worldcupRepository.save(worldcup);
    }

    public WorldcupDto createWorldcup(WorldcupDto dto, String username) {

        Worldcup worldcup = WorldcupDto.createWorldcup(dto, username);
        Worldcup created = worldcupRepository.save(worldcup);

        return Worldcup.createWorldcupDto(created);
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
}
