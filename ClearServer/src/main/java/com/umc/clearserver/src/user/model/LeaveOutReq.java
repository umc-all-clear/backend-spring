package com.umc.clearserver.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaveOutReq {
    private String email;
    private String jwt;
}
