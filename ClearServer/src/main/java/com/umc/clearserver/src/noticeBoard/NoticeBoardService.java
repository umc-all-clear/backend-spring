package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.config.BaseResponse;
import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.noticeBoard.model.*;
import com.umc.clearserver.src.utils.AES128;
import com.umc.clearserver.src.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@Service

public class NoticeBoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NoticeBoardDao noticeBoardDao;
    private final NoticeBoardService noticeBoardService;
    private final JwtService jwtService;

    @Autowired
    public NoticeBoardService(NoticeBoardDao noticeBoardDao, NoticeBoardService noticeBoardService, JwtService jwtService){
        this.noticeBoardDao = noticeBoardDao;
        this.noticeBoardService = noticeBoardService;
        this.jwtService = jwtService;
    }

    public PostNoticeBoardRes postToNoticeBoard(PostNoticeBoardReq postNoticeBoardReq) throws BaseException {
        try {
            PostNoticeBoardRes postNoticeBoardRes = NoticeBoardDao.postToNoticeBoard(postNoticeBoardReq);
            return postNoticeBoardRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
