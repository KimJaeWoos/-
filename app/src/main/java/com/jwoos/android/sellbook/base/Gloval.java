package com.jwoos.android.sellbook.base;

/**
 * Created by 컴대문 on 2016-03-20.
 */
public class Gloval {

    private static int setting_count = 0;
    private static String user_nik;
    private static String noti_book_id;
    private static String base_image_url_uploads = "http://fack14.cafe24.com/SellBook/uploads/";
    private static String base_image_url_profile = "http://fack14.cafe24.com/SellBook/profile/";

    public static String getNoti_book_id() {
        return noti_book_id;
    }

    public static void setNoti_book_id(String noti_book_id) {
        Gloval.noti_book_id = noti_book_id;
    }

    public static String getBase_image_url_uploads() {
        return base_image_url_uploads;
    }

    public static String getBase_image_url_profile() {
        return base_image_url_profile;
    }

    public static int getSetting_count() {
        return setting_count;
    }

    public static void setSetting_count(int setting_count) {
        Gloval.setting_count = setting_count;
    }

    public static String getUser_nik() {
        return user_nik;
    }

    public static void setUser_nik(String user_nik) {
        Gloval.user_nik = user_nik;
    }


}
