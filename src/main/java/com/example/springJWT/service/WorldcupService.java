package com.example.springJWT.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.example.springJWT.config.S3Config;
import com.example.springJWT.dto.WorldcupDto;
import com.example.springJWT.entity.Worldcup_WC;
import com.example.springJWT.repository.WorldcupRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WorldcupService {

    private final WorldcupRepository worldcupRepository;
    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public WorldcupService(WorldcupRepository worldcupRepository, S3Config s3Config){

        this.worldcupRepository = worldcupRepository;
        this.s3Config = s3Config;
    }

    public List<WorldcupDto> getWorldCupIndex() {

        List<Worldcup_WC> worldcupList = worldcupRepository.findAll();
        List<WorldcupDto> worldcupDtoList = new ArrayList<>();
        for(Worldcup_WC w : worldcupList){
            WorldcupDto dto = Worldcup_WC.createWorldcupDto(w);
            worldcupDtoList.add(dto);
        }
        return worldcupDtoList;
    }

    public void likesCountUp(Long id) {

        Worldcup_WC worldcup = worldcupRepository.findById(id).orElse(null);
        worldcup.setLikesCount(worldcup.getLikesCount() + 1L);
        worldcupRepository.save(worldcup);
    }

    public WorldcupDto createWorldcup(WorldcupDto dto, String username) {

        Worldcup_WC worldcup = WorldcupDto.createWorldcup(dto, username);
        Worldcup_WC created = worldcupRepository.save(worldcup);

        return Worldcup_WC.createWorldcupDto(created);
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
}
