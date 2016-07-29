package com.jwoos.android.sellbook.base.retrofit.model;

import android.print.PrinterId;

/**
 * Created by Jwoo on 2016-06-11.
 */
public class Book_Info {
    private String user_profile_img_path;
    private String user_nik;
    private String kind_count_out;
    private String kind_count;
    private String book_category;
    private String book_price;
    private String book_content;
    private String book_phone;
    private String book_image1;
    private String book_image2;
    private String book_image3;
    private String book_register_time;
    private String sold_kind;
    private String book_name;
    private String book_id;
    private String sell_count;
    private String comment_count;


    private String favorite_count;
    private String user_chk;

    private String favorite_chk;

    public String getFavorite_chk() {
        return favorite_chk;
    }

    public void setFavorite_chk(String favorite_chk) {
        this.favorite_chk = favorite_chk;
    }

    public void setUser_chk(String user_chk) {
        this.user_chk = user_chk;
    }


    public String getUser_chk() {
        return user_chk;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(String favorite_count) {
        this.favorite_count = favorite_count;
    }

    public String getSell_count() {
        return sell_count;
    }

    public void setSell_count(String sell_count) {
        this.sell_count = sell_count;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getEmpty_chk() {
        return empty_chk;
    }

    public void setEmpty_chk(String empty_chk) {
        this.empty_chk = empty_chk;
    }

    private String empty_chk;

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }
    public String getBook_category() {
        return book_category;
    }

    public void setBook_category(String book_category) {
        this.book_category = book_category;
    }

    public String getBook_price() {
        return book_price;
    }

    public void setBook_price(String book_price) {
        this.book_price = book_price;
    }

    public String getBook_content() {
        return book_content;
    }

    public void setBook_content(String book_content) {
        this.book_content = book_content;
    }

    public String getBook_phone() {
        return book_phone;
    }

    public void setBook_phone(String book_phone) {
        this.book_phone = book_phone;
    }

    public String getBook_image1() {
        return book_image1;
    }

    public void setBook_image1(String book_image1) {
        this.book_image1 = book_image1;
    }

    public String getBook_image2() {
        return book_image2;
    }

    public void setBook_image2(String book_image2) {
        this.book_image2 = book_image2;
    }

    public String getBook_image3() {
        return book_image3;
    }

    public void setBook_image3(String book_image3) {
        this.book_image3 = book_image3;
    }

    public String getBook_register_time() {
        return book_register_time;
    }

    public void setBook_register_time(String book_register_time) {
        this.book_register_time = book_register_time;
    }

    public String getSold_kind() {
        return sold_kind;
    }

    public void setSold_kind(String sole_kind) {
        this.sold_kind = sole_kind;
    }

    public String getUser_profile_img_path() {
        return user_profile_img_path;
    }

    public void setUser_profile_img_path(String user_profile_img_path) {
        this.user_profile_img_path = user_profile_img_path;
    }

    public String getUser_nik() {
        return user_nik;
    }

    public void setUser_nik(String user_nik) {
        this.user_nik = user_nik;
    }

    public String getKind_count_out() {
        return kind_count_out;
    }

    public void setKind_count_out(String kind_count_out) {
        this.kind_count_out = kind_count_out;
    }

    public String getKind_count() {
        return kind_count;
    }

    public void setKind_count(String kind_count) {
        this.kind_count = kind_count;
    }

}
