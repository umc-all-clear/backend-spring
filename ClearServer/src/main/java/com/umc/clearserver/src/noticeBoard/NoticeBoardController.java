package com.umc.clearserver.src.noticeBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class NoticeBoardController {

    private final AwsS3Service awsS3Service;

    @PostMapping("/images")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        awsS3Service.upload(multipartFile, "static");
        return "test";
    }
    @GetMapping("/images")
    public String testConnection(){
        return "hellow World!";
    }
}
