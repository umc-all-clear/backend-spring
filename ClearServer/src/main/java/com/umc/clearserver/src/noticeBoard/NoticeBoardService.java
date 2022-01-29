package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.utils.AES128;
import com.umc.clearserver.src.noticeBoard.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@Service

public class NoticeBoardService {
    private NoticeBoardDao noticeBoardDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    public NoticeBoardService(NoticeBoardDao noticeBoardDao)
    {
        this.noticeBoardDao = noticeBoardDao;
    }

    public PostNoticeBoardRes postToNoticeBoard(String beforePicUrl, String afterPicUrl, String userID) throws BaseException {
        try{
            PostNoticeBoardRes postNoticeBoardRes = noticeBoardDao.postNoticeBoardRes(beforePicUrl, afterPicUrl, userID);
            return postNoticeBoardRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }


    }


}
