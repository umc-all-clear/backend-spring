package com.umc.clearserver.src.friend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetFriendRankingRes {
    private int friendIndex;
    private String friendEmail;
    private double score;
}
