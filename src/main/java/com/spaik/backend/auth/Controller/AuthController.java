// 회원가입, 로그인 api요청을 받아 서비스에 전달

package com.spaik.backend.auth.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.spaik.backend.auth.dto.LoginRequestDto;
import com.spaik.backend.auth.dto.SignupRequestDto;
import com.spaik.backend.auth.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.spaik.backend.auth.service.AuthService;
import com.spaik.backend.auth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthService oauthService;

    // 자체 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    // 자체 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto request) {
        TokenDto token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    // 소셜 로그인 시작
    @GetMapping("/oauth2/authorization/{socialLoginType}")
    public void socialLoginType(@PathVariable String socialLoginType, HttpServletResponse response) throws IOException {
        String redirectUrl = oauthService.request(socialLoginType);
        response.sendRedirect(redirectUrl);
    }

    // 소셜 로그인 콜백
    @GetMapping("/login/oauth2/code/{socialLoginType}")
    public ResponseEntity<?> callback(
        @PathVariable String socialLoginType,
        @RequestParam String code
    ) {
        TokenDto result = oauthService.requestAccessToken(socialLoginType, code);
        return ResponseEntity.ok(result);
    }
}