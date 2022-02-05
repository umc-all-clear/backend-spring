package com.umc.clearserver.src.user;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.config.secret.Secret;
import com.umc.clearserver.src.user.model.*;
import com.umc.clearserver.src.utils.AES128;
import com.umc.clearserver.src.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

/**
 * Service란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Create, Update, Delete 의 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
            // [Business Layer]는 컨트롤러와 데이터 베이스를 연결
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }
    // ******************************************************************************
    // 회원가입(POST)
    public PostSignUpRes createUser(PostSignUpReq postSignUpReq) throws BaseException {
<<<<<<< Updated upstream
        // 중복 확인: 해당 이메일을 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
        System.out.println(userProvider.checkEmail(postSignUpReq.getEmail()));
=======
>>>>>>> Stashed changes
        if (userProvider.checkEmail(postSignUpReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try {
            // 암호화: postUserReq에서 제공받은 비밀번호를 보안을 위해 암호화시켜 DB에 저장합니다.
            // ex) password123 -> dfhsjfkjdsnj4@!$!@chdsnjfwkenjfnsjfnjsd.fdsfaifsadjfjaf
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postSignUpReq.getPassword1()); // 암호화코드
            postSignUpReq.setPassword1(pwd);
        } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        System.out.println(pwd);
        try {
            int userIdx = userDao.createUser(postSignUpReq);
            System.out.println(userIdx);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            String email = postSignUpReq.getEmail();
            String nickname = postSignUpReq.getNickname();
            return new PostSignUpRes(userIdx, email, nickname, jwt);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    // 회원정보 수정(Patch)
//    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
//        try {
//            int result = userDao.modifyUserName(patchUserReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
//            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
//                throw new BaseException(MODIFY_FAIL_USERNAME);
//            }
//        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    // 회원 reported 횟수 추가
//    public void modifyReportedCnt(PatchUserReq patchUserReq) throws BaseException{
//        try{
//            int result = userDao.reportUserCnt(patchUserReq);
//            if(result == 0){//업데이트 실패기
//                throw new BaseException(MODIFY_FAIL_REPORT);
//            }
//        }
//        catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    //해당 email을 갖는 User의 정보 삭제
//    public int deleteUserByEmail(LeaveOutReq leaveOutReq) throws BaseException{
//        // 회원 조회
//        int userId;
//        try{
//            userId = userDao.getUsersById(leaveOutReq.getEmail());
//            System.out.println(userId);
//        }
//        catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//        // 회원 jwt랑 받은 jwt와 비교
//        String jwt = leaveOutReq.getJwt();
//        try {
//            if (jwtService.getUserIdx() == userId){
//
//            }
//        }
//        catch (Exception exception){
//            throw new BaseException(INVALID_JWT)
//        }
//        if (userProvider.checkEmail(postSignUpReq.getEmail()) == 1) {
//            throw new BaseException(POST_USERS_EXISTS_EMAIL);
//        }
//        try {
//            int deleteUserRes = userDao.deleteUserByEmail(userEmail);
//            return deleteUserRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
    //++++++++++++++++++++++++++++++++++++++++++++++
    //전준휘의 영역 끝!
    //++++++++++++++++++++++++++++++++++++++++++++++

    //++++++++++++++++++++++++++++++++++++++++++++++
    //송해광님의 영역
    //++++++++++++++++++++++++++++++++++++++++++++++

    //++++++++++++++++++++++++++++++++++++++++++++++
    //송해광님의 영역 끝!
    //++++++++++++++++++++++++++++++++++++++++++++++

    //++++++++++++++++++++++++++++++++++++++++++++++
    //김현주님의 영역!
    //++++++++++++++++++++++++++++++++++++++++++++++

    //++++++++++++++++++++++++++++++++++++++++++++++
    //김현주님의 영역 끝!
    //++++++++++++++++++++++++++++++++++++++++++++++


    //++++++++++++++++++++++++++++++++++++++++++++++
    //김영진님의 영역!
    //++++++++++++++++++++++++++++++++++++++++++++++

    //++++++++++++++++++++++++++++++++++++++++++++++
    //김영진님의 영역 끝!
    //++++++++++++++++++++++++++++++++++++++++++++++
}
