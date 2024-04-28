package com.auth.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Profile {
    private int id;
    private int userId;
    private String firstName;
    private String lastName;
    private char gender;
    private Date dob;
    private String address;
    private Integer phone;
    private String imagePath;

    User user;

    @Autowired
    public Integer setUserId(){
        return this.userId = user.getId();
    }
}
