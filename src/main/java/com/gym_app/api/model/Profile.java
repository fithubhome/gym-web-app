package com.gym_app.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Profile {
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID userId;

    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;
    private String address;
    private String phone;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    public Profile(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}

