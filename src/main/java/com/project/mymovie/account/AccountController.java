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

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "account/sign-up-form";
    }

    /**
     *
     * @param signUpForm
     * @param errors 에러가 있다면 폼화면을 다시 보여준다.
     * @return 에러가 없다면 회원 정보를 저장, 인증메일을 전송하고 자동로그인을 시키면서 메인페이지를 보여준다.
     */
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

    // CONTROLLER -> SERVICE -> REPOSITORY
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
        model.addAttribute("account", account);
        model.addAttribute("nickname", account.getNickname());
        return view;
    }

    /**
     * @param account
     * @param model 로그인한 유저가 가입한 아이디를 보여준다.
     * @return 이메일을 재전송할 수 있는 뷰를 보여준다.
     */
    @GetMapping("/check-email")
    public String checkEmail(@CurrentAccount Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/resend-email";
    }

    /**
     * 인증메일은 1시간에 한 번만 전송할 수 있다.
     * @param account 인증메일을 전송한지 1시간 이전인지 확인하고, 유저가 가입한 이메일을 보여준다.
     * @param model 유효하지 않을 경우 에러와 이메일을 보여준다.
     * 인증메일을 보낼 수 있다면, 재전송을 하고
     * @return 메인페이지로 보낸다.
     */
    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentAccount Account account, Model model){
        if (!account.canResendEmail()) {
            model.addAttribute("error","인증 이메일은 1시간에 한 번만 전송할 수 있습니다." );
            model.addAttribute("email", account.getEmail());
            return "account/resend-email";
        }
            accountService.sendSignUpVerifyEmail(account);
            return "redirect:/";
    }
}
