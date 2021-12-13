package com.project.mymovie.account;

import com.project.mymovie.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원정보를 저장한 후 회원가입 인증 이메일을 보내서 회원가입을 완료시키는 메소드
     *
     * @param signUpForm
     */
    public Account saveAccountAndSendEmail(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailVerificationToken();
        sendSignUpVerifyEmail(newAccount);
        return newAccount;
    }


    /**
     * 회원 회원가입 정보 저장
     *
     * @param signUpForm (회원가입 폼)
     * @return 입력 받은 데이터를 저장
     */
    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .movieUpdatedByEmail(true)
                .movieUpdatedByWeb(true)
                .build();
//        Account newAccount = accountRepository.save(account);
        return accountRepository.save(account);
    }

    /**
     * newAccount에게 회원가입 인증 이메일 전송
     *
     * @param newAccount (회원이 입력한 데이터를 저장한 newAccount)
     */
    public void sendSignUpVerifyEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("회원 가입 인증,");
        mailMessage.setText("/check-email-link?token=" + newAccount.getEmailVerificationToken() +
                "&email=" + newAccount.getEmail());
        javaMailSender.send(mailMessage);
    }

    /**
     * 인증을 마친 계정을 회원가입이 완료됨과 동시에 자동으로 로그인시킨다.
     *
     * @param account
     */
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);

    }

}
