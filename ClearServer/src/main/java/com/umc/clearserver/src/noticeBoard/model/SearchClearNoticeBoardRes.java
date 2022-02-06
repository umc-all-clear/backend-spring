package com.umc.clearserver.src.noticeBoard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchClearNoticeBoardRes {
    private int postId;
    private String cleanedAt;
    private String userEmail;
    private double score;
    private String contents;
    private String comments;
    private String beforePicUrl;
    private String afterPicUrl;
    private boolean isWaited;
}
