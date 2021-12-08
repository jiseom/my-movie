package com.project.mymovie.domain;



import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter @Getter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private String emailToken;

    private LocalDateTime emailTokenCreatedDate;

    private boolean emailVerified;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private LocalDateTime joinAt;

    @Lob
    private String image;

    private String bio;

    private String url;

    private boolean movieUpdatedByEmail;

    private boolean movieUpdatedByWeb;



}
