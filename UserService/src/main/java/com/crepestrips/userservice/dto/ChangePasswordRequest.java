package com.crepestrips.userservice.dto;

public class ChangePasswordRequest {

    private String oldPassword;
    private String newPassword;
    private String userName;

    public ChangePasswordRequest(String oldPassword, String newPassword, String userName) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    
}
