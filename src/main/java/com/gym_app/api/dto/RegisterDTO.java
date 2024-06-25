package com.bodystats.api.dto;

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
