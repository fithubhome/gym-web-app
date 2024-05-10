// Profile.java
package com.auth.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;
    private String address;
    private String phone;
    private String imagePath;

    public Profile(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }
}
