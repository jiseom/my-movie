package com.project.mymovie.account;

import com.project.mymovie.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;


    /**
     * 회원정보를 저장한 후 회원가입 인증 이메일을 보내서 회원가입을 완료시키는 메소드
     * @param signUpForm
     */
    public void saveAccountAndSendEmail(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailVerificationToken();
        sendSignUpVerifyEmail(newAccount);
    }


    /**
     * 회원 회원가입 정보 저장
     * @param signUpForm (회원가입 폼)
     * @return 입력 받은 데이터를 저장
     */
    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword())
                .movieUpdatedByEmail(true)
                .movieUpdatedByWeb(true)
                .build();
//        Account newAccount = accountRepository.save(account);
        return accountRepository.save(account);
    }


    /**
     * newAccount에게 회원가입 인증 이메일 전송
     * @param newAccount (회원이 입력한 데이터를 저장한 newAccount)
     */
    private void sendSignUpVerifyEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("회원 가입 인증,");
        mailMessage.setText("/verify-email-token?token=" + newAccount.getEmailVerificationToken() +
                "&email=" + newAccount.getEmail());
        javaMailSender.send(mailMessage);
    }

}