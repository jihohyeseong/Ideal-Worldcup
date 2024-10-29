package com.example.springJWT.controller;

import com.example.springJWT.dto.CustomUserDetails;
import com.example.springJWT.dto.WorldcupDto;
import com.example.springJWT.service.WorldcupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/worldcup")
public class WorldcupController {

    private final WorldcupService worldcupService;

    public WorldcupController(WorldcupService worldcupService){

        this.worldcupService = worldcupService;
    }

    @GetMapping("")
    public ResponseEntity<List<WorldcupDto>> worldcupIndex(){

        List<WorldcupDto> dtoList = worldcupService.getWorldCupIndex();

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PostMapping("/{id}/likes")
    public void likesCountUp(@PathVariable Long id){

        worldcupService.likesCountUp(id);
    }

    @PostMapping("")
    public ResponseEntity<WorldcupDto> createWorldcup(@RequestPart("data") WorldcupDto dto,
                                                      @RequestPart("image") MultipartFile image,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        String username = userDetails.getUsername();
        String imageUrl = worldcupService.saveImage(image);
        dto.setImageUrl(imageUrl);
        WorldcupDto worldcupDto = worldcupService.createWorldcup(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(worldcupDto);
    }

}
