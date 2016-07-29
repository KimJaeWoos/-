package com.jwoos.android.sellbook.base.retrofit.model;

import com.google.gson.annotations.Expose;

public class Login {

    @Expose
    private String status;
    @Expose
    private String login;
    private String user_nic;

    public String getUser_nic() {

        return user_nic;
    }

    public void setUser_nic(String user_nic) {
        this.user_nic = user_nic;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
}