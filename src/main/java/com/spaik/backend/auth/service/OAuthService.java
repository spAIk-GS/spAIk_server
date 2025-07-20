//외부 소셜로그인 처리 서비스
package com.spaik.backend.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaik.backend.auth.dto.TokenDto;
import com.spaik.backend.auth.jwt.JwtTokenProvider;
import com.spaik.backend.user.entity.Role;
import com.spaik.backend.user.entity.User;
import com.spaik.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;


    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1. 구글 인증 URL 리턴
    public String request(String socialLoginType) {
        if (!"google".equalsIgnoreCase(socialLoginType)) throw new IllegalArgumentException("google만 구현됨");

        String baseUrl = "https://accounts.google.com/o/oauth2/v2/auth";
        String scope = "profile email";
        String responseType = "code";
        String url = baseUrl
                + "?response_type=" + responseType
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=" + scope
                + "&access_type=offline";

        return url;
    }

    // 2. 구글 code로 토큰 + 유저정보 → JWT발급
    public TokenDto requestAccessToken(String socialLoginType, String code) {
        if (!"google".equalsIgnoreCase(socialLoginType)) throw new IllegalArgumentException("google만 구현됨");

        // 1) 구글 액세스 토큰 받기
        String tokenUrl = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, String.class);

        String accessToken;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(tokenResponse.getBody());
            accessToken = node.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("구글 토큰 파싱 실패: " + e.getMessage());
        }

        // 2) 액세스 토큰으로 구글 유저정보 조회
        String userinfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        HttpEntity<?> userRequest = new HttpEntity<>(userHeaders);
        ResponseEntity<String> userResponse = restTemplate.exchange(
                userinfoUrl, HttpMethod.GET, userRequest, String.class);

        String email, name;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode userInfo = mapper.readTree(userResponse.getBody());
            email = userInfo.get("email").asText();
            name = userInfo.has("name") ? userInfo.get("name").asText() : "";
        } catch (Exception e) {
            throw new RuntimeException("구글 유저정보 파싱 실패: " + e.getMessage());
        }

        // 3) DB에 유저 등록/조회
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.orElseGet(() -> userRepository.save(
                User.builder()
                        .email(email)
                        .name(name)
                        .password("") // No password for 소셜
                        .role(Role.USER)
                        .build()
        ));

        // 4) JWT 발급 및 반환
        String jwt = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());

        return new TokenDto(jwt, user.getEmail(), user.getRole().name());
    }
}

