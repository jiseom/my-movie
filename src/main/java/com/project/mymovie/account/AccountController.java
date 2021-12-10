package com.project.mymovie.account;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "account/sign-up-form";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up-form";
        }
        //회원가입 과정 -> 회원이 입력한 회원정보 저장과 이메일 인증 메일 발송
        accountService.saveAccountAndSendEmail(signUpForm);

        return "redirect:/";
    }
}
