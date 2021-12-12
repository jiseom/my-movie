package com.project.mymovie.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
//로그인한 사용자에 대한 정보를 참조하고 싶을 때, 커스텀 한 Principal 을 사용 할 수 있다.
public @interface CurrentAccount {
}
