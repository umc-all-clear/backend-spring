package com.umc.clearserver.src.user.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAllUserRes {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private Timestamp createdAt;
}
