package com.project.mymovie.account;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name="persistent_logins")
@Entity
@Getter@Setter
public class PersistentLogins {

    /**
     * persistent token approach
     */
    @Id
    @Column(length = 64)
    private String series; //랜덤한 고정된 토큰값

    @Column(nullable = false, length = 64)
    private String username;

    @Column(nullable = false,length = 64)
    private String token;

    @Column(nullable = false,name = "last_used")
    private LocalDateTime lastUsed;


}
