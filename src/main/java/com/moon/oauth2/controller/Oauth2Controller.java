package com.moon.oauth2.controller;

import com.google.gson.Gson;
import com.moon.oauth2.model.OauthToken;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
//토큰이 생성되었는지 확인하기 위한 redirect주소 처리 controller
public class Oauth2Controller {

    @Autowired
    private Gson gson;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/callback")
    public OauthToken callbackSocial(@RequestParam String code) {
        String credentials = "testClientId:testSecret";
        //Base64란 바이트 스트림을 ASCII영역의 문자들로만 이루어진 일련의 문자열로 바꾸는 인코딩 방식. credentials(유니코드 문자열)을 getBytes로 바이트 코드로 인코딩한 후 사용한다.
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        //httpheader부분의 contect-type정의.
        //MediaType.APPLICATION_FORM_URLENCODED은 &으로 분리되고, "=" 기호로 값과 키를 연결하는 key-value tuple로 인코딩되는 값
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); //key하나에 여러개의 value를 가질 수 있다.
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "http://localhost:8081/oauth2/callback");

        //http프로토콜을 이용하는 통신의 header, body 관련 정보를 저장한다. (body, header)순.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        //HttpEntity 상속 클래스. url로 post요청을 보내고 결과를 string타입으로 받아 ResponseEntity에 담아준다.
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8081/oauth/token", request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), OauthToken.class);
        }
        return null;
    }

    @GetMapping(value = "/token/refresh") //토큰 갱신 여부 확인
    public OauthToken refreshToken(@RequestParam String refreshToken) {

        String credentials = "testClientId:testSecret";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE); //json타입의 데이터를 지정
        headers.add("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8081/oauth/token", request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), OauthToken.class);
        }
        return null;
    }
}




