package com.umc.clearserver.src.noticeBoard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class DeleteNoticeBoardRes {
    private String deleteResult;
    private int deletedPostIdx;
    private String beforePicDeleteRes;
    private String afterPicDeleteRes;
}
