package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.utils.AES128;
import com.umc.clearserver.src.noticeBoard.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@Service

public class NoticeBoardProvider {
    private static NoticeBoardDao noticeBoardDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    public NoticeBoardProvider(NoticeBoardDao noticeBoardDao)
    {
        this.noticeBoardDao = noticeBoardDao;
    }

    public static List<SearchClearNoticeBoardRes> viewNoticeBoard(SearchClearNoticeBoardReq searchClearNoticeBoardReq, String email) throws BaseException{
        try{
            List<SearchClearNoticeBoardRes> searchClearNoticeBoardRes = noticeBoardDao.viewNoticeBoard(searchClearNoticeBoardReq, email);
            return searchClearNoticeBoardRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
