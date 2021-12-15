package com.project.mymovie.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class MyMovieList {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Movie movie;

    //어느 회원의 찜목록인지
    @ManyToOne
    private Account account;

    private LocalDateTime addCreatedDate;


}
