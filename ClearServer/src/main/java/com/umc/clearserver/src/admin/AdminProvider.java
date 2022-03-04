package com.umc.clearserver.src.admin;

import com.google.api.services.storage.Storage;
import com.umc.clearserver.src.admin.model.GetUnscoredNoticeBoardRes;
import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.config.BaseResponse;
import com.umc.clearserver.src.friend.FriendDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@Service
public class AdminProvider {
    private final AdminDao adminDao;

    public AdminProvider(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<GetUnscoredNoticeBoardRes> getUnscoredUser() throws BaseException {
        try{
            List<GetUnscoredNoticeBoardRes> getUnscoredNoticeBoardRes = adminDao.getUnscoredUser();
            return getUnscoredNoticeBoardRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
