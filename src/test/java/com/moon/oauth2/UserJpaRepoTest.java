package com.moon.oauth2;

import com.moon.oauth2.entity.User;
import com.moon.oauth2.repo.UserJpaRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserJpaRepoTest {

    @Autowired
    private UserJpaRepo userJpaRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertNewUser() {
        userJpaRepo.save(User.builder()
                .uid("moon@email.com")
                .password(passwordEncoder.encode("1234"))
                .name("moon")
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }
}
