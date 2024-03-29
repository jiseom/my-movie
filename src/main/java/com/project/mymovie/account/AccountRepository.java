package com.project.mymovie.account;


import com.project.mymovie.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {
    //이메일 존재 여부 확인
    boolean existsByEmail(String email);
    //닉네임 존재 여부 확인
    boolean existsByNickname(String nickname);
    //이메일 조회
    Account findByEmail(String email);
    //닉네임 조회
    Account findByNickname(String nickname);
}
