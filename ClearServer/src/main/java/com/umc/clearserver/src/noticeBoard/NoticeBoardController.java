package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.config.BaseResponse;
import com.umc.clearserver.src.noticeBoard.model.PostNoticeBoardReq;
import com.umc.clearserver.src.noticeBoard.model.PostNoticeBoardRes;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;
    private final AwsS3Service awsS3Service;

    /**
     * 게시판에 Before, After 사진 게시하고 평가 대기하기
     * @throws IOException
     */
    @PostMapping("/noticeboard/offering")
    public BaseResponse<PostNoticeBoardRes> postToNoticeBoard(@RequestParam("beforePic") MultipartFile multipartFile1, @RequestParam("afterPic") MultipartFile multipartFile2,
                                                   @RequestParam("jsonRequest") String jsonParam) throws IOException {
        JSONObject jObject = new JSONObject(jsonParam);
        String userId = (String)jObject.get("clientID");
        PostNoticeBoardReq postNoticeBoardReq = new PostNoticeBoardReq(userId);

        String beforeImgUrl = awsS3Service.upload(multipartFile1, userId);
        String afterImgUrl = awsS3Service.upload(multipartFile2, userId);
        try{
            PostNoticeBoardRes postNoticeBoardRes = noticeBoardService.postToNoticeBoard(beforeImgUrl, afterImgUrl, userId);
            return new BaseResponse<>(postNoticeBoardRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}
