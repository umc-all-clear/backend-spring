package com.umc.clearserver.src.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair {
    public Integer first;
    public String second;
    public Integer third;

    public Pair(Integer first, String second, Integer third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
