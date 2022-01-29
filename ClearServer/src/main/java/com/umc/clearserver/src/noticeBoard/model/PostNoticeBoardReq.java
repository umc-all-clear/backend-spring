package com.umc.clearserver.src.noticeBoard.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 해당 클래스의 파라미터가 없는 생성자를 생성, 접근제한자를 PROTECTED로 설정.

public class PostNoticeBoardReq {
    private String userEmail;
}
