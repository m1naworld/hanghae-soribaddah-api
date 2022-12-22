package com.sparta.soundsea.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.soundsea.user.dto.OAuthLoginDto;
import com.sparta.soundsea.user.dto.RequestNaverOauthLoginDto;
import com.sparta.soundsea.user.dto.RequestNaverOauthTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource("classpath:OAuth.properties")
public class NaverOauth {
    @Value("${naver.client.id}")
    private String CLIENT_ID;

    @Value("${naver.client.secret}")
    private String CLIENT_SECRET;

    // 1. 역직렬화를 위한 ObjectMapper 생성
    // ObjectMapper : JSON 컨텐츠를 Java 객체로 deserialization 하거나 Java 객체를 JSON으로 serialization 할 때 사용
    // 비용이 비싸기 때문에, bean/static으로 처리하는 것이 좋다. 어짜피 Component 내부에서 하나 생성이니까 괜찮을듯?
    ObjectMapper objectMapper = new ObjectMapper();

    private RequestNaverOauthTokenDto codeToToken(String code, String state){
        // RestTemplate 인스턴스 생성
        RestTemplate rt = new RestTemplate();

        HttpHeaders TokenHeaders = new HttpHeaders();
        TokenHeaders.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> TokenParams = new LinkedMultiValueMap<>();
        TokenParams.add("grant_type", "authorization_code");
        TokenParams.add("client_id", CLIENT_ID);
        TokenParams.add("client_secret", CLIENT_SECRET);
        TokenParams.add("code", code);	// 응답으로 받은 코드
        TokenParams.add("state", state); // 응답으로 받은 상태

        HttpEntity<MultiValueMap<String, String>> TokenRequest = new HttpEntity<>(TokenParams, TokenHeaders);
        ResponseEntity<String> TokenResponse = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                TokenRequest,
                String.class
        );

        // json -> 객체로 매핑하기 위해 NaverOauthParams 클래스 생성
        RequestNaverOauthTokenDto naverOauthToken = null;
        try {
            naverOauthToken = objectMapper.readValue(TokenResponse.getBody(), RequestNaverOauthTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return naverOauthToken;
    }

    private RequestNaverOauthLoginDto getProfileFromToken(RequestNaverOauthTokenDto naverOauthTokenDto){

        // header를 생성해서 access token을 넣어줍니다.
        HttpHeaders profileRequestHeader = new HttpHeaders();
        profileRequestHeader.add("Authorization", "Bearer " + naverOauthTokenDto.getAccess_token());
        HttpEntity<HttpHeaders> profileHttpEntity = new HttpEntity<>(profileRequestHeader);

        // RestTemplate 인스턴스 생성
        RestTemplate rt = new RestTemplate();

        // profile api로 생성해둔 헤더를 담아서 요청을 보냅니다.
        ResponseEntity<String> profileResponse = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                profileHttpEntity,
                String.class
        );

        RequestNaverOauthLoginDto requestNaverOauthLoginDto = null;
        try {
            requestNaverOauthLoginDto = objectMapper.readValue(profileResponse.getBody(), RequestNaverOauthLoginDto.class);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return requestNaverOauthLoginDto;
    }

    public OAuthLoginDto getLoginDtoFromNaver(String code, String state){
        return getProfileFromToken(codeToToken(code, state)).toNaverLoginDto();
    }

}
