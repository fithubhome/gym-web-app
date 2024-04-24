package com.auth.api;
import com.auth.api.model.User;

public class ApplicationContext {
    private static User currentUser;

    public static void setCurrentUser(User user){
        currentUser = user;
    }
    public static User getCurrentUser(){
        return currentUser;
    }
}
