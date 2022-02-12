package com.umc.clearserver.src.friend;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.friend.model.GetFriendRankingRes;
import com.umc.clearserver.src.friend.model.GetFriendRelationRes;
import com.umc.clearserver.src.user.model.GetUserRes;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<GetFriendRankingRes> getFriendrank(Integer userId, Integer year, Integer month) throws BaseException{
        try{
            List<GetFriendRankingRes> getFriendIndexRes = friendDao.getFriendRank(userId, year, month);
            return getFriendIndexRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 userIdx를 갖는 User의 정보 조회
    public GetFriendRelationRes getFriendRelation(int user1, int user2) throws BaseException {
        try {
            GetFriendRelationRes getFriendRelationRes = friendDao.getFriendRelation(user1, user2);
            return getFriendRelationRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}