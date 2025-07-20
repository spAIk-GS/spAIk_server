//OAuth2User정보 로드
package com.spaik.backend.auth.oauth;

import com.spaik.backend.user.entity.User;
import com.spaik.backend.user.entity.Role;
import com.spaik.backend.user.repository.UserRepository;
import com.spaik.backend.auth.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;  // 이 부분 추가

    //provider(구글)에서 사용자 정보 가져옴
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // DB에 없으면 자동 회원가입
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            return userRepository.save(User.builder()
                .email(email)
                .name(name)
                .password("") // 소셜 로그인은 비밀번호 없음
                .role(Role.USER)
                .build());
        });


        // JWT 토큰 발급
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());

        // 반환할 OAuth2User 객체에 토큰 포함
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("token", token);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }
}
