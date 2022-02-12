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
    @GetMapping("") // (GET) http://localhost:23628/friend
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String email) {
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

    /**
     * 회원 1명 조회 API
     * [GET] /friends/:id
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{id}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetFriendRes> getFriend(@PathVariable("id") int id) {
        // @PathVariable RESTful(URL)에서 명시된 파라미터({})를 받는 어노테이션, 이 경우 userId값을 받아옴.
        //  null값 or 공백값이 들어가는 경우는 적용하지 말 것
        //  .(dot)이 포함된 경우, .을 포함한 그 뒤가 잘려서 들어감
        // Get Users
        try {
            GetFriendRes getFriendRes = friendProvider.getFriend(id);
            return new BaseResponse<>(getFriendRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 친구 관계 조회 API
     * [GET] /friends
     *
     * 또는
     *
     * 해당 닉네임을 같는 유저들의 정보 조회 API
     * [GET] /friends? userId=
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("") // (GET) 127.0.0.1:9000/friends
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetFriendRes>> getFriends(@RequestParam(required = false) Integer userId) {
        //  @RequestParam은, 1개의 HTTP Request 파라미터를 받을 수 있는 어노테이션(?뒤의 값). default로 RequestParam은 반드시 값이 존재해야 하도록 설정되어 있지만, (전송 안되면 400 Error 유발)
        //  지금 예시와 같이 required 설정으로 필수 값에서 제외 시킬 수 있음
        //  defaultValue를 통해, 기본값(파라미터가 없는 경우, 해당 파라미터의 기본값 설정)을 지정할 수 있음
        try {
            if (userId == null) { // query string인 nickname이 없을 경우, 그냥 전체 유저정보를 불러온다.
                List<GetFriendRes> getFriendListRes = friendProvider.getFriends();
                return new BaseResponse<>(getFriendListRes);
            }
            // query string인 nickname이 있을 경우, 조건을 만족하는 유저정보들을 불러온다.
            List<GetFriendRes> getFriendListRes = friendProvider.getFriendsByUserId(userId);
            return new BaseResponse<>(getFriendListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
