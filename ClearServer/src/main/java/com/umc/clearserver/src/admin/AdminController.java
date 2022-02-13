package com.umc.clearserver.src.admin;

import com.umc.clearserver.src.admin.model.PostEvaluateReq;
import com.umc.clearserver.src.admin.model.admin;
import com.umc.clearserver.src.config.BaseException;
import com.umc.clearserver.src.config.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.umc.clearserver.src.config.BaseResponseStatus.*;

@RequestMapping("/admin")
@RestController
public class AdminController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.


    @Autowired
    private final AdminProvider adminProvider;
    @Autowired
    private final AdminService adminService;

    public AdminController(AdminProvider adminProvider, AdminService adminService) {
        this.adminProvider = adminProvider;
        this.adminService = adminService;
    }

    /**
     * 관리자가 score 추가하기
     * [PATCH] /admin/evaluate
     */
    @ResponseBody
    @PatchMapping("/evaluate")    // POST 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<String> evaluate(@RequestBody admin admin) throws BaseException {
        //  @RequestBody란, 클라이언트가 전송하는 HTTP Request Body(우리는 JSON으로 통신하니, 이 경우 body는 JSON)를 자바 객체로 매핑시켜주는 어노테이션
        try {
            if (admin.getId()==null) {
                return new BaseResponse<>(POST_ADMIN_INVALID_SCORE);
            }
            PostEvaluateReq postEvaluateReq = new PostEvaluateReq(admin.getId(), admin.getScore(), admin.getComments());
            adminService.evaluate(postEvaluateReq);

            String result = "평점이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
