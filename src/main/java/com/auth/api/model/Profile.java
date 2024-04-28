package com.auth.api.model;

import com.auth.api.UserContext;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Profile {
    private int id;
    private int userId;
    private String firstName;
    private String lastName;
    private char gender;
    private Date dob;
    private String address;
    private String phone;
    private String imagePath;

    public Profile(HttpSession session){
        String sessionId = (String) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        this.userId = currentUser.getId();
        this.firstName = currentUser.getEmail().split("@")[0];
    }

}
