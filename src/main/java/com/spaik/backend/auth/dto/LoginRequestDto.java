//Login 시 입력값 DTO
package com.spaik.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {
    
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    // 생성자 필요 시 추가
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
