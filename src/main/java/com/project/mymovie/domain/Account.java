package com.project.mymovie.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

    private String emailVerificationToken;

    private LocalDateTime emailTokenCreatedDate;

    private boolean emailVerified;

    private boolean enabled;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private LocalDateTime joinAt;

    @Lob
    private String image;

    private String bio;

    private String url;

    private boolean movieUpdatedByEmail;

    private boolean movieUpdatedByWeb;

    /**
     * 회원가입 인증 토큰 생성 (무작위로 생성된 고유한 값을 가질 것) /
     * 토큰 생성과 동시에 토큰 시간을 기록
     */
    public void generateEmailVerificationToken() {
        this.emailVerificationToken = UUID.randomUUID().toString();
        this.emailTokenCreatedDate = LocalDateTime.now();
    }

    /**
     * 토큰 생성 시간이 현재시간 보다 한시간 전 이상이어야 확인 메일을 보낼 수 있다.
     * (날짜 비교)
     */
    public boolean canResendEmail() {
        return this.emailTokenCreatedDate.isBefore(LocalDateTime.now().minusHours(1));
    }

    public void completeSignUp() {
        this.setEmailVerified(true);
        this.setEnabled(true);
        this.setJoinAt(LocalDateTime.now());
    }

    /**
     *
     * @param token url파라미터로 받은 토큰값
     * @return 이메일 인증을 받으려는 해당 계정의 토큰값과 파라미터로 받아온 토큰값이 일치하는지 여부를 boolean으로 반환
     */
    public boolean isValidToken(String token) {
     return this.getEmailVerificationToken().equals(token);

    }
}
