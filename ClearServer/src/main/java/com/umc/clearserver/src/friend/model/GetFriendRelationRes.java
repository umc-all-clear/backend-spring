package com.umc.clearserver.src.friend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFriendRelationRes {
    private int state;
    private String friendNickname;
    private String friendEmail;
    private int friendIdx;
}