package fr.eseo.example.androidproject.api;

import java.io.Serializable;

public class UserModel implements Serializable {

    private int userId;
    private String userForename;
    private String userSurname;

    public UserModel(int userId, String userForename, String userSurname) {
        this.userId = userId;
        this.userForename = userForename;
        this.userSurname = userSurname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserForename() {
        return userForename;
    }

    public void setUserForename(String userForename) {
        this.userForename = userForename;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }
}
