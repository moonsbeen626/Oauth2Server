package com.moon.oauth2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//토큰 정보를 받을 모델
public class OauthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;
}
