package com.gym_app.api.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RegisterDTO {
    private String email;
    private String password;
}
