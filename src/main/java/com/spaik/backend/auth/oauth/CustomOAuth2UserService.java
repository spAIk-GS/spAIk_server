//OAuth2User정보 로드
package com.spaik.backend.auth.oauth;

import com.spaik.backend.user.entity.AuthProvider;
import com.spaik.backend.user.entity.Role;
import com.spaik.backend.user.entity.User;
import com.spaik.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String providerId = (String) attributes.get("sub"); // 구글 고유 ID
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(name, email, providerId));

        return oAuth2User;
    }

    private User createUser(String name, String email, String providerId) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(null) // 소셜 로그인은 비밀번호 없음
                .role(Role.USER)
                .provider(AuthProvider.GOOGLE)
                .providerId(providerId)
                .build();

        return userRepository.save(user);
    }
}
