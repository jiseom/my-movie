package com.project.mymovie.account;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter //TODO 필요한 어노테이션 더 넣기
public class SignUpForm {

    @Email
    @NotBlank
    private String email;

    @NotBlank
//    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;

    @NotBlank
    @Length(min = 4, max = 10) //TODO :비밀번호 정규식으로 변경
    //자주쓰이는 정규식 적어도 소문자 하나, 대문자 하나, 숫자 하나가 포함되어 있는 문자열(8글자 이상 15글자 이하) -
    //올바른 암호 형식을 확인할 때 사용될 수 있음: /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,15}/
    private String password;

}
