package com.umc.clearserver.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PostSignUpRes {
    private int id;
    private String email;
    private String nickname;
    private String jwt;
}
