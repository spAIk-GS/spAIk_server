package com.spaik.backend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.spaik.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spaik.backend.auth.dto.SignupRequestDto;
import com.spaik.backend.auth.dto.LoginRequestDto;
import com.spaik.backend.auth.dto.TokenDto;
import com.spaik.backend.auth.jwt.JwtTokenProvider;
import com.spaik.backend.user.entity.User;
import com.spaik.backend.user.entity.Role;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입 처리
    public void signup(SignupRequestDto dto) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        // 비밀번호 암호화 및 회원 생성
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .name(dto.getName())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }


    public TokenDto login(LoginRequestDto dto) {
        //이메일로 사용자 존재여부 조회
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        //비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        //JWT 토큰 생성 및 결과 반환
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
        return new TokenDto(token, user.getEmail(), user.getRole().name());
    }
}
