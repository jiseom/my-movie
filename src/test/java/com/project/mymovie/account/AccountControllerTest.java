package com.project.mymovie.account;

import com.project.mymovie.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
    void invalid_email()throws Exception{

        Account account = Account.builder()
                .email("jiseon@na.com")
                .nickname("지선")
                .password("123456789")
                .build();
        accountRepository.save(account);


        mockMvc.perform(post("/sign-up")
                        .param("email", "jiseon@na.com")//중복 이메일
                        .param("nickname", "지선1")
                        .param("password", "1234567891")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up-form"));
                //에러메시지가 포함된 입력폼을 다시 보여주게 된다.

    }

}