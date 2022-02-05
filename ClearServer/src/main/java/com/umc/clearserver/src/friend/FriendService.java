package com.umc.clearserver.src.friend;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.config.secret.Secret;
import com.umc.clearserver.src.friend.model.PatchFriendReq;
import com.umc.clearserver.src.user.UserDao;
import com.umc.clearserver.src.user.UserProvider;
import com.umc.clearserver.src.user.model.PostSignUpReq;
import com.umc.clearserver.src.user.model.PostSignUpRes;
import com.umc.clearserver.src.utils.AES128;
import com.umc.clearserver.src.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@Service
public class FriendService {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    private final FriendDao friendDao;
    private final FriendProvider friendProvider;

    @Autowired
    public FriendService(FriendDao friendDao, FriendProvider friendProvider) {
        this.friendDao = friendDao;
        this.friendProvider = friendProvider;
    }

    // 회원정보 삭제(Patch)
    public void deleteFriend(PatchFriendReq patchFriendReq) throws BaseException {
        try {
            int result = friendDao.deleteFriend(patchFriendReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
