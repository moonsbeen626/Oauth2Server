package com.moon.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//공통 환경 설정
public class WebMvcConfig implements WebMvcConfigurer { //viewResolver설정 등과 같은 기본적인 서블릿 설정을 하는 클래스

    private static final long MAX_AGE_SECONDS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) { //CORS요청 활성화
        registry.addMapping("/**") //CORS적용할 URL패턴 지정
                .allowedOrigins("*") //자원 공유를 허락할 Origin 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE") //허용할 http메소드
                .allowCredentials(true) //응답code를 노출할지 안할지 설정.
                .maxAge(MAX_AGE_SECONDS); //preflight(실제 요청 전에 인증 헤더를 전송하여 서버의 허용 여부를 미리 체크하는 테스트 요청)를 캐시에 저장할 시간
    }

    @Bean
    public RestTemplate getRestTemplate() { //http서버와의 통신을 단순화, restful원칙. 동기방식.
        return new RestTemplate();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); //BCryptPasswordEncoder를 사용해 PasswordEncoder를 만들어 반환
    }
}
