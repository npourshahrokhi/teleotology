package com.doctor.ashanet.ashanet;

public class Users {
    String UserUsername;
    String userId;
    String userPassword;
    String userToken;

    public Users(String userId2, String userUsername, String userPassword2, String userToken2) {
        this.userId = userId2;
        this.UserUsername = userUsername;
        this.userPassword = userPassword2;
        this.userToken = userToken2;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public String getUserUsername() {
        return this.UserUsername;
    }

    public void setUserUsername(String userUsername) {
        this.UserUsername = userUsername;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword2) {
        this.userPassword = userPassword2;
    }

    public String getUserToken() {
        return this.userToken;
    }

    public void setUserToken(String userToken2) {
        this.userToken = userToken2;
    }
}
