package com.bodystats.api.security;

import com.bodystats.api.dto.UserDTO;
import com.bodystats.api.model.UserEntity;
import com.bodystats.api.dto.RoleDTO;
import com.bodystats.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServices implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(CustomUserDetailsServices.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.getByEmail(email);
        if (userOptional.isEmpty()) {
            logger.severe("User not found: " + email);
            throw new UsernameNotFoundException("Email or Password not found");
        }

        UserEntity user = userOptional.get();

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new RoleDTO(role.getId(), role.getName())).collect(Collectors.toList())
        );

        Collection<GrantedAuthority> authorities = userDTO.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        return new CustomUserDetails(
                userDTO.getEmail(),
                userDTO.getPassword(),
                authorities
        );
    }
}
