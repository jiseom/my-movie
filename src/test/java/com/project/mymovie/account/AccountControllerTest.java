package com.project.mymovie.account;

import com.project.mymovie.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.InOrderImpl;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

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
        then(javaMailSender).should().send(any(SimpleMailMessage.class));

    }

}