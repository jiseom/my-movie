package com.project.mymovie.account;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {
    @GetMapping("/sign-up")
    public String signUpForm() {
        return "account/sign-up-form";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up-form";
        }
        //TODO: 회원가입 과정 -> 이메일 인증 메일 발송
        return "redirect:/";
    }
}
