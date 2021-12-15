package com.project.mymovie.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Review {

    @Id @GeneratedValue
    private Long id;

    /**
     * 리뷰는 특정한 영화안에서 만들어진다.
     * 어느 영화에 속한 리뷰인지? review -> movie @ManyToOne 단방향
     */
    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Account account;

    private String title;

    private String description;

    private int reviewRate;



}
