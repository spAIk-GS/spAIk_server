package com.spaik.backend.user.controller;

import com.spaik.backend.user.entity.User;
import com.spaik.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<User> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    // 내 정보 수정
    @PutMapping("/me")
    public ResponseEntity<User> updateMyInfo(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody User updateUser) {

        String email = userDetails.getUsername();
        User updated = userService.updateUserByEmail(email, updateUser);
        return ResponseEntity.ok(updated);
    }

    // 내 정보 삭제
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
