package com.project.mymovie.account;

import com.project.mymovie.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
//@Transactional(readOnly=true)
public class MyUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     *
     * @param emailOrNickname
     * @return userAccount 객체를 반환
     * @throws UsernameNotFoundException 이메일이나 닉네임을 찾지 못할 경우 예외를 던진다.
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }
        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }
        log.info("Success find account {}", account);
        return new UserAccount(account);
    }
}
