package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.noticeBoard.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@Service

public class NoticeBoardService {
    private static NoticeBoardDao noticeBoardDao;
    private final AwsS3Service awsS3Service;

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    public NoticeBoardService(NoticeBoardDao noticeBoardDao, AwsS3Service awsS3Service)
    {
        this.noticeBoardDao = noticeBoardDao;
        this.awsS3Service = awsS3Service;
    }

    public PostNoticeBoardRes postToNoticeBoard(String beforePicUrl, String afterPicUrl, String userID, String userContent) throws BaseException {
        try{
            PostNoticeBoardRes postNoticeBoardRes = noticeBoardDao.postNoticeBoardRes(beforePicUrl, afterPicUrl, userID, userContent);
            return postNoticeBoardRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public static DeleteNoticeBoardRes deleteNoticeBoard(int noticeIdx, String email) throws BaseException{
        try{
            DeleteNoticeBoardRes deleteNoticeBoardRes = noticeBoardDao.deleteNoticeBoard(noticeIdx, email);
            return deleteNoticeBoardRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
