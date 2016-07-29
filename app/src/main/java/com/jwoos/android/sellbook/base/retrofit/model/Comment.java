package com.jwoos.android.sellbook.base.retrofit.model;

/**
 * Created by 컴대문 on 2016-07-13.
 */
public class Comment {
    private String cmt_time;
    private String cmt_content;
    private String user_nik;
    private String user_profile;
    private String user_chk;
    private String comment_id;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getCmt_time() {
        return cmt_time;
    }

    public void setCmt_time(String cmt_time) {
        this.cmt_time = cmt_time;
    }

    public String getCmt_content() {
        return cmt_content;
    }

    public void setCmt_content(String cmt_content) {
        this.cmt_content = cmt_content;
    }

    public String getUser_nik() {
        return user_nik;
    }

    public void setUser_nik(String user_nik) {
        this.user_nik = user_nik;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUser_chk() {
        return user_chk;
    }

    public void setUser_chk(String user_chk) {
        this.user_chk = user_chk;
    }
}
