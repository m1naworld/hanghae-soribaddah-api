package com.sparta.soundsea.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sparta.soundsea.user.dto.OAuthLoginDto;
import com.sparta.soundsea.user.mapper.UserMapper;
import com.sparta.soundsea.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.sparta.soundsea.common.exception.ExceptionMessage.SOCIAL_LOGIN_ERROR_MSG;

@Service
@Slf4j
public class UserKakaoService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private OAuthLoginDto oAuthLoginDto;


    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private String grantType = "authorization_code";

    public String getKakaoAccessToken(String code) {
        System.out.println(code);
        String access_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true); // post 요청을 위해 true 설정

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            StringBuilder sb = new StringBuilder(); // 요청 url
            sb.append("grant_type=" + grantType);
            sb.append("&client_id=" + clientId);
            sb.append("&redirect_uri=" + redirectUri);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("KakaoAccessToken-ResponseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonElement element = getResponseJson(br);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();

           log.info("Kakao-Accesstoken : " + access_Token);

            br.close();
            bw.close();
            return access_Token;

        } catch (IOException e) {
            throw new RuntimeException(SOCIAL_LOGIN_ERROR_MSG.getMsg());
        }


    }

    // 카카오에서 준 access_token을 이용하여 사용자 정보 조회
    public OAuthLoginDto createKakaoUser(String token) {
        try {
            String reqURL = "https://kapi.kakao.com/v2/user/me";

            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            log.info("create-KakaoUser-responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonElement element = getResponseJson(br);

            String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            String email = "";

            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();

            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            log.info("kakao-user-nickname: " + nickname);
            log.info("kakao-user-email : " + email);

            br.close();
            return new OAuthLoginDto(email, nickname);
        } catch (IOException e) {
            throw new RuntimeException(SOCIAL_LOGIN_ERROR_MSG.getMsg());
        }

    }

    //요청을 통해 얻은 Response 메세지 JSON 파싱
    public JsonElement getResponseJson(BufferedReader br) throws IOException {

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        log.info("kakao response body : " + result);

        //Gson 라이브러리로 JSON파싱
        JsonElement element = JsonParser.parseString(result);

        return element;
    }
}
