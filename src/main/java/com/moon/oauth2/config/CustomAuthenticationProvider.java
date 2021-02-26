package com.moon.oauth2.config;

import com.moon.oauth2.entity.User;
import com.moon.oauth2.repo.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j //구현체 종류에 상관 없이 일관된 로깅 코드를 작성
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider { //로그인 정보의 유효성을 검증

    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepo userJpaRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        //회원 이름으로 DB를 조회한다.
        User user = userJpaRepo.findByUid(name).orElseThrow(() -> new UsernameNotFoundException("user is not exists"));

        if(!passwordEncoder.matches(password, user.getPassword())) //회원 정보의 비밀번호와 입력된 비밀번호의 매칭 여부를 확인한다.
            throw new BadCredentialsException("password is not valid");

        //UsernamePasswordAuthenticationToken은 요청에 대한 인증을 담당. username, password, 권한 정보를 조합해 인증 정보를 생성.
        return new UsernamePasswordAuthenticationToken(name, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
