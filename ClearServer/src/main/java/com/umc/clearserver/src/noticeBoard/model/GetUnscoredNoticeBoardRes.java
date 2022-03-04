package com.umc.clearserver.src.noticeBoard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor

public class GetUnscoredNoticeBoardRes {
    private int id;
    private Timestamp cleanedTime;
    private String userEmail;
    private Double score;
    private String contents;
    private String comments;
    private String beforePicUrl;
    private String afterPicUrl;
    private Boolean isWaited;
}
