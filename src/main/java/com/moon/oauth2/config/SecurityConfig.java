package com.moon.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity//웹 보안 활성화. WebSecurityConfigurerAdapter를 구현하였을 때만 유용하다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//사용자 저장소를 설정.
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //인터셉터로 요청을 안전하게 보호하는 방법을 설정.
        http
                .csrf().disable() //테스트 용도이므로 csrf 사용안함
                .headers()
                    .frameOptions()
                    .disable() // 사용시 h2콘솔 접근 막히므로 disable처리
                    .and()
                .authorizeRequests()
                    .antMatchers("/oauth/**", "/oauth/token", "/oauth2/**", "/h2-console/*").permitAll()
                    .and()
                .formLogin() //.loginPahe()를 설정하지 않는 경우 기본 로그인 폼을 사용.
                    .and()
                .httpBasic(); //http기본 인증 활성화.
    }
}
