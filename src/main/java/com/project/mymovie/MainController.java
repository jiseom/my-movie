package com.project.mymovie;

import com.project.mymovie.account.AccountRepository;
import com.project.mymovie.account.CurrentAccount;
import com.project.mymovie.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AccountRepository accountRepository;

    @GetMapping("/")
    public String mainPage(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "account/login";
    }


}
