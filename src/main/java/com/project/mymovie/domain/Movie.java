package com.project.mymovie.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Account account;

    private String title;//영화 제목

    @Lob
    private String posterImage;//영화 포스터 이미지

    @Lob
    private String bannerImage;//영화 백그라운드 이미지

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres = new HashSet<>(); //장르

    private LocalDateTime releaseYear; //개봉년도

    private String country; //제작 국가

    private int bookingRate; //예매율

    private int filmRating; // 평점

    private String runtime; //상영 시간

    private String director;//감독


}
