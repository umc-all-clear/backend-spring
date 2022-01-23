package com.umc.clearserver.src.noticeBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class NoticeBoardController {

    private final AwsS3Service awsS3Service;

    /**
     * 게시판에 Before, After 사진 게시하고 평가 대기하기
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/noticeboard/offering")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        awsS3Service.upload(multipartFile, "static");
        return "test";
    }
}
