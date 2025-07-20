//회원가입 요청 DTO
package com.spaik.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter 
@Setter
public class SignupRequestDto {
    
    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String passwordCheck;
}
