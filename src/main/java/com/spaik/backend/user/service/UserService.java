package com.spaik.backend.user.service;

import com.spaik.backend.user.entity.User;
import com.spaik.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 이메일로 사용자 조회
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 이메일 기반 사용자 정보 수정
    public User updateUserByEmail(String email, User updatedUser) {
        User user = findByEmail(email);
        user.setName(updatedUser.getName());
        user.setRole(updatedUser.getRole());
        user.setProvider(updatedUser.getProvider());
        user.setProviderId(updatedUser.getProviderId());
        // 필요한 필드만 업데이트 하세요 (비밀번호 등은 별도 처리 권장)
        return userRepository.save(user);
    }

    // 이메일 기반 사용자 삭제
    public void deleteUserByEmail(String email) {
        User user = findByEmail(email);
        userRepository.delete(user);
    }

}
