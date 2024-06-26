package com.gym_app.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String email;
    private String password;
    private List<RoleDTO> roles;
}
