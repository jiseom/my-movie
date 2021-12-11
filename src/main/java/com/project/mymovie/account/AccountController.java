package com.project.mymovie.account;

import com.project.mymovie.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "account/sign-up-form";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up-form";
        }
        //회원가입 과정 -> 이메일 인증 메일 발송
        Account account = accountService.saveAccountAndSendEmail(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-link")
    public String checkEmailLink(String email, String token, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";

        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }
        account.completeSignUp();
        accountService.login(account);
        model.addAttribute("nickname", account.getNickname());
        return view;
    }


}
