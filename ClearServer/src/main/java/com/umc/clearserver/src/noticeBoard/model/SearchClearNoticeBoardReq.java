package com.umc.clearserver.src.noticeBoard.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class SearchClearNoticeBoardReq {
    private int year;
    private int month;
}
