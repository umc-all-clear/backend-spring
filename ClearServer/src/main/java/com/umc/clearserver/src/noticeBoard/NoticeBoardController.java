package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.config.BaseResponse;
import com.umc.clearserver.src.noticeBoard.model.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
                                                   @RequestParam("jsonRequest") String jsonParam, @RequestParam("jsonRequestContent") String jsonContent) throws IOException {
        JSONObject jObject = new JSONObject(jsonParam);
        String userId = (String)jObject.get("clientID");

        JSONObject jObjectContent = new JSONObject(jsonContent);
        String userContent = (String)jObjectContent.get("content");
        PostNoticeBoardReq postNoticeBoardReq = new PostNoticeBoardReq(userId);

        String beforeImgUrl = awsS3Service.upload(multipartFile1, userId);
        String afterImgUrl = awsS3Service.upload(multipartFile2, userId);
        try{
            PostNoticeBoardRes postNoticeBoardRes = noticeBoardService.postToNoticeBoard(beforeImgUrl, afterImgUrl, userId, userContent);
            return new BaseResponse<>(postNoticeBoardRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 특정 유저의 특정 날짜 청소시간 확인하기
     * [POST] /noticeboard/user?email=
     */
    @PostMapping("/noticeboard/user")
    public BaseResponse<List<SearchClearNoticeBoardRes>> viewNoticeBoard(@RequestBody SearchClearNoticeBoardReq searchClearNoticeBoardReq, @RequestParam(required = true) String email){
        try{
            List<SearchClearNoticeBoardRes> searchClearNoticeBoardRes = NoticeBoardProvider.viewNoticeBoard(searchClearNoticeBoardReq, email);
            return new BaseResponse<>(searchClearNoticeBoardRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 특정 게시물 삭제하기
     * [Delete] /noticeboard/user?deletePostIndex=
     */
    @DeleteMapping("/noticeboard/user")
    public BaseResponse<DeleteNoticeBoardRes> deleteNoticeBoard(@RequestParam(required = true) int deletePostIndex, String email){
        try{
            DeleteNoticeBoardRes deleteNoticeBoardRes = NoticeBoardService.deleteNoticeBoard(deletePostIndex, email);
            return new BaseResponse<>(deleteNoticeBoardRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 유저의 청소안된 게시물 가져오기
     * [Post] /noticeboard/unchecked/user=
     */
    @PostMapping("/noticeboard/unchecked")
    public BaseResponse<List<GetUnscoredNoticeBoardRes>> getUnchecked(@RequestParam(required = true) String email){
        try{
            List<GetUnscoredNoticeBoardRes> getUnscoredNoticeBoardResList = NoticeBoardProvider.getUnchecked(email);
            return new BaseResponse<>(getUnscoredNoticeBoardResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
