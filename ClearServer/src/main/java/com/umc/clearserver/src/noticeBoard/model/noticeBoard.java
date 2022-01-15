package com.umc.clearserver.src.noticeBoard.model;
import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(userIdx, nickname, email, password)를 받는 생성자를 생성

public class noticeBoard{
    private int id;
    private String beforePic;
    private String afterPic;
    private String contents;
    private String comments;
    private int writer;
    private float score;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private LocalDate cleanedAt;
    private boolean isWaited;
}