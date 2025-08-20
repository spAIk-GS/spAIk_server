package com.spaik.backend.global.config;

import com.spaik.backend.auth.jwt.JwtAuthenticationFilter;
import com.spaik.backend.auth.jwt.JwtTokenProvider;
import com.spaik.backend.auth.oauth.CustomOAuth2UserService;
import com.spaik.backend.auth.oauth.OAuth2SuccessHandler;
import com.spaik.backend.auth.jwt.CustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                
                                // 아래 경로는 인증 없이 허용
                                "/auth/register",
                                "/auth/login",
                                "/login/oauth2/code/**",
                                "/error",
                                "/videos/presign",
                                "/videos/presign-get",
                                "/thumbnails/presign",
                                "/thumbnails/presign-get",

                                "/analysis/callback",
                                "/analysis/callback/**",
                                "/analysis/subscribe/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )

                //추가함
                // For API calls, return 401 JSON rather than redirecting to login
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"error\":\"Unauthorized\"}");
                }))

                // JWT 인증 필터(swagger, 회원가입/로그인 등 제외)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService) {
                    @Override
                    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
                        String path = request.getRequestURI();
                        return
                                path.startsWith("/swagger-ui") ||
                                        path.startsWith("/v3/api-docs") ||
                                        path.startsWith("/swagger-resources") ||
                                        path.startsWith("/webjars") ||
                                        path.equals("/auth/register") ||
                                        path.equals("/auth/login") ||
                                        path.startsWith("/auth/oauth2") ||
                                        path.equals("/videos/presign") ||
                                        path.equals("/thumbnails/presign") ||
                                        path.equals("/analysis/callback") ||
                                        path.startsWith("/analysis/callback/");
                    }
                }, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //추가함
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Frontend dev origin
        config.setAllowedOriginPatterns(List.of("http://localhost:5173"));

        // Methods used by your app
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers used by your app (Authorization for Bearer, Content-Type for JSON)
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));

        // If you rely on cookies/session, set true and DO NOT use '*'
        // For Bearer token only, false is fine
        config.setAllowCredentials(false);

        // Cache preflight for 1 hour
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
