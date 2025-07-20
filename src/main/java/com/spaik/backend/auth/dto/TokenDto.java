//로그인 성공 시 반환할 JWT 액세스 토큰
package com.spaik.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private String token;
    private String email;
    private String role;
}
