package com.sparta.soundsea.security.jwt;

import com.sparta.soundsea.security.CustomUserDetailsService;
import com.sparta.soundsea.security.jwt.exception.CustomSecurityException;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.entity.UserRole;
import com.sparta.soundsea.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.sparta.soundsea.common.exception.ExceptionMessage.TOKEN_NOT_FOUND_MSG;
import static com.sparta.soundsea.common.exception.ExceptionMessage.USER_NOT_FOUND_ERROR_MSG;

@Slf4j      // Console에 log 찍으려고 사용
@Component  // Bean 등록
@PropertySource("classpath:security.properties")
@RequiredArgsConstructor
public class JwtUtil {
    // AccessToken Key 값
    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    // RefreshToken Key 값
    public static final String AUTHORIZATION_REFRESH = "RefreshToken";
    // 사용자 권한 Key 값
    public static final String AUTHORIZATION_KEY = "auth";
    // 토큰 생성 시 앞에 붙는 식별자
    private static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료 시간 (분*초*밀리sec)
    private static final long TOKEN_TIME = 30 * 60 * 1000L;
    // 의존성 주입
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    // JWT SecretKey
    @Value("${jwt.secret.key.access}")
    private String accessTokenSecretKey;
    @Value("${jwt.secret.key.refresh}")
    private String refreshTokenSecretKey;

    private Key accessTokenKey;
    private Key refreshTokenKey;

    // 사용할 암호화 알고리즘 설정
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] accessTokenBytes = Base64.getDecoder().decode(accessTokenSecretKey); // JWT 토큰 값을 Base64 형식을 디코딩
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenBytes); // key에 넣어줌

        byte[] refreshTokenBytes = Base64.getDecoder().decode(refreshTokenSecretKey); // JWT 토큰 값을 Base64 형식을 디코딩
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenBytes); // key에 넣어줌
    }

    // Token 분해 Method
    public String resolveToken(HttpServletRequest request, String authorization) {
        String bearerToken = request.getHeader(authorization); // AccessToken의 value를 가지고옴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // Bearer 값 있는지 확인
            return bearerToken.substring(7); // 확인되면 Bearer 빼고 반환
        }
        return null;
    }

    // Access Token 생성
    public String createAccessToken(String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + // BEARER 앞에 붙여주기
                Jwts.builder()
                        .setSubject(username) // subject 부분에 username 넣기
                        .claim(AUTHORIZATION_KEY, role) // claim 부분에 사용자 권한 키와 역할 넣기
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 언제 생성되었는지
                        .signWith(accessTokenKey, signatureAlgorithm) // Key를 어떻게 암호화 할 것인지
                        .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken() {
        Date date = new Date();
        return BEARER_PREFIX + // BEARER 앞에 붙여주기
                Jwts.builder()
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME + 30 * 60 * 1000L)) // 만료 시간(+30분)
                        .setIssuedAt(date) // 언제 생성되었는지
                        .signWith(refreshTokenKey, signatureAlgorithm) // Key를 어떻게 암호화 할 것인지
                        .compact();
    }

    // Access 토큰 검증
    public boolean validateAccessToken(String accessToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(accessToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid Access JWT signature, 유효하지 않는 Access JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            // 만료된 경우 토큰 재발급
            log.info("Expired Access JWT, 만료된 Access JWT 입니다.");
            accessTokenReissuance(request, response);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported Access JWT, 지원되지 않는 Access JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("Access JWT claims is empty, 잘못된 Access JWT 입니다.");
        }
        return false;
    }

    // Refresh 토큰 검증
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(refreshTokenKey).build().parseClaimsJws(refreshToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid Refresh JWT signature, 유효하지 않는 Refresh JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired Refresh JWT, 만료된 Refresh JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported Refresh JWT, 지원되지 않는 Refresh JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("Refresh JWT claims is empty, 잘못된 Refresh JWT 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromHttpServletRequest(HttpServletRequest request) throws CustomSecurityException {
        // Request에서 Token 가져오기
        String token = resolveToken(request, AUTHORIZATION_ACCESS);

        // Token이 있는지 확인
        if (token == null) {
            // Handler 대신에 Handler Filter를 사용하므로, CustomException 재정의
            throw new CustomSecurityException(TOKEN_NOT_FOUND_MSG);
        }

        try {
            return Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token).getBody();
        }
        catch(ExpiredJwtException ex){
            // JWT 만료로 인한 Exception 발생 시 Exception에서 Claim 그냥 빼옴
            return ex.getClaims();
        }
    }

    private void accessTokenReissuance(HttpServletRequest request, HttpServletResponse response) {
        // 1. request에서 accessToken 분해 후, 사용자 이름 찾아옴.
        Claims info = getUserInfoFromHttpServletRequest(request);
        System.out.println("username : " + info.getSubject());
        // 2. 사용자 이름을 통해 Member Entity 불러옴
        User user = userRepository.findByLoginId(info.getSubject())
                .orElseThrow(()-> new CustomSecurityException(USER_NOT_FOUND_ERROR_MSG));
        // 3. MemberEntity에 등록된 Access Token과 Refresh Token이 Request에 담긴 값과 일치하는 지 확인
        if (user.getAccessToken().substring(7).equals(resolveToken(request, "AccessToken"))
                && user.getRefreshToken().substring(7).equals(resolveToken(request, "RefreshToken"))){
            // 4. RefreshToken 유효성 검사
            if (validateRefreshToken(resolveToken(request, "RefreshToken"))){
                System.out.println("JWT 재발급 조건 충족");
                String newAccessToken = createAccessToken(user.getLoginId(), user.getRole());
                response.addHeader(JwtUtil.AUTHORIZATION_ACCESS, newAccessToken);
                // 5. Member Data 최신화
                user.updateToken(newAccessToken, user.getRefreshToken());
                userRepository.save(user);
            }
        }
        // 6. 로그인 페이지 반환
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String memberName) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(memberName); // 이름을 통해 사용자 조회
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //userDetail 및 권한 넣어 생성
    }

}
