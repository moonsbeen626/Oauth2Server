package com.moon.oauth2.service;

import com.moon.oauth2.entity.User;
import com.moon.oauth2.repo.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
//spring security에서 DB에서 유저 정보를 불러오는 UserDetailsService인터페이스를 상속바다 사용자 정보를 받아오는 역할을 한다.
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepo userJpaRepo;
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker(); //user의 상태를 확인할 수 있음. 인증 정보가 만료되지 않았는지 등을 확인할 수 있음.

    //DB에서 유저 정보를 가져오는 중요한 메소드. User형으로 사용자의 정보를 받아오면 된다.
    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = userJpaRepo.findByUid(name).orElseThrow(() -> new UsernameNotFoundException("user is not exists"));
        detailsChecker.check(user); //user에 접근할 수 있고 인증 정보 만료되지 않았고, 사용 가능한지등등... 확인.
        return user;
    }
}
