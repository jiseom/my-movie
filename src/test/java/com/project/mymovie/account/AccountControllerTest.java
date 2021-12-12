package com.project.mymovie.account;

import com.project.mymovie.domain.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void beforeEach() {
        accountRepository.deleteAll();
    }

    @BeforeEach
    public void passwordEncoder() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @DisplayName("회원 가입 화면이 보이는지 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up-form"));
    }

    @DisplayName("이메일 중복으로 인한 회원가입 실패 테스트")
    @Test
    void invalid_email() throws Exception {

        Account account = Account.builder()
                .email("jiseon@email.com")
                .nickname("지선")
                .password("123456789")
                .build();
        accountRepository.save(account);


        mockMvc.perform(post("/sign-up")
                        .param("email", "jiseon@email.com")//중복 이메일
                        .param("nickname", "지선1")
                        .param("password", "1234567891")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())//모델안에 에러가 있는지 검사
                .andExpect(view().name("account/sign-up-form"));
        //에러메시지가 포함된 입력폼을 다시 보여주게 된다.

    }

    @DisplayName("회원가입 인증 메일 보내기 테스트")
    @Test
    void success_sendSignUpVerifyEmail() throws Exception {

        mockMvc.perform(post("/sign-up")
                        .param("email", "jiseon@email.com")//중복 이메일
                        .param("nickname", "지선1")
                        .param("password", "12345678")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        Account account = accountRepository.findByEmail("jiseon@email.com");
        assertNotNull(account);
        assertNotNull(account.getEmailVerificationToken());
//        assertNotEquals(account.getPassword(),"12345678");
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        assertThat(passwordEncoder.matches(account.getPassword(), encodedPassword)).isTrue();
        assertThat(encodedPassword).contains("{bcrypt}");
        then(javaMailSender).should().send(any(SimpleMailMessage.class));

    }

    @DisplayName("이메일 인증과 가입 실패")
    @Test
    void wrongEmailLink_before_fail_signUp() throws Exception {
        mockMvc.perform(get("/check-email-link")
                        .param("token", "fsdfds12315")
                        .param("email", "jiseon@email.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"));
    }

    @DisplayName("이메일 인증 성공 및 가입 완료")
    @Test
    void completeSignUp() throws Exception {
        Account account = Account.builder()
                .email("jiseon@email.com")
                .nickname("지선")
                .password("12345678")
                .build();
        accountRepository.save(account);
        account.generateEmailVerificationToken();
        //트랜잭션을 걸어주어야 accountRepository.save(newAccount); 로 업데이트 되는것
        mockMvc.perform(get("/check-email-link")
                        .param("token", account.getEmailVerificationToken())
                        .param("email", account.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated().withUsername("지선"))
                .andExpect(model().attributeExists("account"));
        Account newAccount = accountRepository.findByEmail(account.getEmail());
        accountService.login(newAccount);
        assertTrue(newAccount.isEmailVerified());
        assertTrue(newAccount.isEnabled());
        assertNotNull(newAccount.getJoinAt());
    }

    @DisplayName("1시간 간격으로 이메일 인증 재전송")
    @Test
    void resendEmail() throws Exception {
        Account account = Account.builder()
                .email("jiseon@email.com")
                .nickname("지선")
                .password("12345678")
                .emailTokenCreatedDate(LocalDateTime.now().minusHours(2))
                .build();
        accountRepository.save(account);
        accountService.login(account);
        mockMvc.perform(get("/resend-confirm-email"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }



}