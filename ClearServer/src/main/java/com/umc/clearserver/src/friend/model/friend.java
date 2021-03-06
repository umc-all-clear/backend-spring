package com.umc.clearserver.src.friend.model;

import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(userIdx, nickname, email, password)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class friend {
    private int id;
    private int user1;
    private int user2;
    private int isAccepted;
    private int state;
}