package com.umc.clearserver.src.admin;

import com.umc.clearserver.src.admin.model.PostEvaluateReq;
import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.friend.FriendDao;
import com.umc.clearserver.src.friend.FriendProvider;
import com.umc.clearserver.src.friend.model.CreateFriendReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@Service
public class AdminService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    private final AdminDao adminDao;
    private final AdminProvider adminProvider;

    @Autowired
    public AdminService(AdminDao adminDao, AdminProvider adminProvider) {
        this.adminDao = adminDao;
        this.adminProvider = adminProvider;
    }
    public void evaluate(PostEvaluateReq postEvaluateReq) throws BaseException {
        try {
            int result = adminDao.evaluate(postEvaluateReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(POST_ADMIN_INVALID_SCORE);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
