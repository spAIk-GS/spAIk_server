package com.spaik.backend.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Swagger 관련 경로 예외 처리
        if (path.startsWith("/swagger-ui") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-resources") ||
            path.startsWith("/webjars") ||
            path.startsWith("/configuration")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 추출 (Authorization 헤더에서)
        String token = jwtTokenProvider.resolveToken(request);

        // 토큰이 있고, 유효하면
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 이메일 추출
            String email = jwtTokenProvider.getEmailFromToken(token);
            
            // DB에서 UserDetails 조회
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 인증 객체(Authentication) 생성
            var authentication = jwtTokenProvider.getAuthentication(userDetails);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Security Context에 등록 → 이 요청은 인증된 사용자로 취급
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 나머지 필터 계속 진행
        filterChain.doFilter(request, response);
    }
}
