package com.umc.clearserver.src.friend;

import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.config.BaseResponse;
import com.umc.clearserver.src.user.model.GetUserRes;
import com.umc.clearserver.src.user.model.PostSignUpReq;
import com.umc.clearserver.src.user.model.PostSignUpRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.umc.clearserver.src.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.umc.clearserver.src.friend.model.*;
import java.util.List;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;
import static com.umc.clearserver.src.utils.ValidationRegex.isRegexEmail;

@RequestMapping("/friends")
@RestController
public class FriendController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.


    @Autowired
    private final FriendProvider friendProvider;
    @Autowired
    private final FriendService friendService;

    public FriendController(FriendProvider friendProvider, FriendService friendService) {
        this.friendProvider = friendProvider;
        this.friendService = friendService;
    }

    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("") // (GET) http://localhost:23628/friends
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false,value = "email") String email) {
        //  @RequestParam은, 1개의 HTTP Request 파라미터를 받을 수 있는 어노테이션(?뒤의 값). default로 RequestParam은 반드시 값이 존재해야 하도록 설정되어 있지만, (전송 안되면 400 Error 유발)
        //  지금 예시와 같이 required 설정으로 필수 값에서 제외 시킬 수 있음
        //  defaultValue를 통해, 기본값(파라미터가 없는 경우, 해당 파라미터의 기본값 설정)을 지정할 수 있음
        try {
            if (email == null) { // query string인 email 없을 경우, 그냥 전체 유저정보를 불러온다.
                List<GetUserRes> getUsersRes = friendProvider.getUsers();
                System.out.println(" email을 입력해 주시기 바랍니다.");
                return new BaseResponse<>(getUsersRes);
            }
            // query string인 email이 있을 경우, 조건을 만족하는 유저정보들을 불러온다.
            List<GetUserRes> getUsersRes = friendProvider.getUsersByEmail(email);
            return new BaseResponse<>(getUsersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 비활성화 API
     * [PATCH] /friends/delete/{friend.id}
     */
    @ResponseBody
    @PatchMapping("/delete")
    @Transactional
    public BaseResponse<String> deleteFriend(@RequestBody friend friend) {
        try {
            PatchFriendReq patchFriendReq = new PatchFriendReq(friend.getUser1(), friend.getUser2());
            friendService.deleteFriend(patchFriendReq);

            String result = "친구 삭제 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/create")    // POST 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<CreateFriendReq> createFriend(@RequestBody CreateFriendReq createFriendReq) throws BaseException {
        //  @RequestBody란, 클라이언트가 전송하는 HTTP Request Body(우리는 JSON으로 통신하니, 이 경우 body는 JSON)를 자바 객체로 매핑시켜주는 어노테이션
        if(createFriendReq.getUser1() < 0){
            return new BaseResponse<>(NO_USER_NUMBER_MINUS);
        }
        if(createFriendReq.getUser2() < 0){
            return new BaseResponse<>(NO_USER_NUMBER_MINUS);
        }

        if(createFriendReq.getUser1() == createFriendReq.getUser2()){
            return new BaseResponse<>(SAME_USER_NUMBER);
        }
        CreateFriendReq createFriendRequest  = friendService.createFriend(createFriendReq);

        return new BaseResponse<>(createFriendRequest);
    }

}
