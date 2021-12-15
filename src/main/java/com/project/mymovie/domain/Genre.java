package com.project.mymovie.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Genre {

    ACTION("액션"),
    ANIMATION("애니메이션"),
    DRAMA("드라마"),
    THRILLER("스릴러"),
    FANTASY("판타지"),
    HORROR("공포(호러)"),
    ROMANCE("로맨스"),
    COMEDY("코미디"),
    CRIME("범죄"),
    MYSTERY("미스터리"),
    SF("SF"),
    ADVENTURE("어드벤쳐"),
    HISTORICAL("역사"),
    WESTERN("서부"),
    DOCUMENTARY("다큐멘터리"),
    MUSICAL("뮤지컬"),
    WAR("전쟁"),
    FAMILY("가족");
    private final String name;

    public static Genre nameOf(String name) {
        return Arrays.stream(Genre.values())
                .filter(genre -> genre.getName().equals(name))
                .findFirst()
                .get();
    }
}
