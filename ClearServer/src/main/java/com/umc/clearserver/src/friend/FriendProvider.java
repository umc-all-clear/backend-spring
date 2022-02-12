package com.umc.clearserver.src.friend;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.friend.model.GetFriendRes;
import com.umc.clearserver.src.user.model.GetUserRes;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.umc.clearserver.src.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FriendProvider {

    private final FriendDao friendDao;

    public FriendProvider(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public int checkEmail(String email) throws BaseException {
        try {
            return friendDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = friendDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = friendDao.getUsersByEmail(email);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 userIdx를 갖는 User의 정보 조회
    public GetFriendRes getFriend(int id) throws BaseException {
        try {
            GetFriendRes getFriendRes = friendDao.getFriend(id);
            return getFriendRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Product들의 정보를 조회
    public List<GetFriendRes> getFriends() throws BaseException {
        try {
            List<GetFriendRes> getFriendListRes = friendDao.getFriends();
            return getFriendListRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 nickname을 갖는 User들의 정보 조회
    public List<GetFriendRes> getFriendsByUserId(Integer userId) throws BaseException {
        try {
            List<GetFriendRes> GetFriendRes = friendDao.getFriendsByUserId(userId);
            return GetFriendRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
