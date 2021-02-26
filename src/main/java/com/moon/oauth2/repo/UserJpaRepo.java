package com.moon.oauth2.repo;

import com.moon.oauth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//JpaRepository는 기본적인 crud기능을 제공한다.
public interface UserJpaRepo extends JpaRepository<User, Long> {
    Optional<User> findByUid(String email); //메소드를 호출하는 것 만으로 데이터를 검색할 수 있다.
}
