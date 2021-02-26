package com.moon.oauth2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder // builder를 사용할수 있게 합니다.
@Entity // jpa entity임을 알립니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "user") // 'user' 테이블과 매핑됨을 명시
//speing security에서 사용자의 정보를 담는 UserDetails인터페이스를 상속 받아 DB에서 유저 정보를 가지고 오는 역할을 한다.
public class User implements UserDetails {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msrl;

    @Column(nullable = false, unique = true, length = 50)
    private String uid;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //쓰기만 지원하는 필드. 즉 사용자 생성 요청은 실행되지만, 응답결과 생성시 해당 필드는 제외되어 응답 본문에 표시되지 않음.
    @Column(length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String provider;

    //ElementCollection는 one to many관계를 다룬다.
    @ElementCollection(fetch = FetchType.EAGER) //즉시 로딩한다.
    @Builder.Default //builder로 생성시 기본값 설정.
    private List<String> roles = new ArrayList<>();

    //계정이 가지고 있는 권한 목록을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //계정이 가지고 있는 권한 목록을 return
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    //계정의 이름을 return
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.uid;
    }

    //계정이 만료되면 true, 아니면 false
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있으면 true, 아니면 false
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정의 password가 만료되지 않았으면 true, 아니면 false
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 사용가능하면 true, 아니면 false
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
