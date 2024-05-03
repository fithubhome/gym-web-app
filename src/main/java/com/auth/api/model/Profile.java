package com.auth.api.model;

import com.auth.api.UserContext;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Profile {
    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;
    private String address;
    private String phone;
    private String imagePath;

    public Profile(UUID id, UUID userId){
        this.id=id;
        this.userId=userId;
    }

}
