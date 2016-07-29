package com.jwoos.android.sellbook.base.retrofit.model;

/**
 * Created by Jwoo on 2016-07-11.
 */
public class Favorites {
    private String book_image;
    private String book_name;
    private String book_register_time;
    private String book_price;
    private String comment_count;
    private String favorite_count;
    private String book_content;
    private String sold_kind;
    private String book_id;

    public String getBook_image() {
        return book_image;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getBook_register_time() {
        return book_register_time;
    }

    public String getBook_price() {
        return book_price;
    }

    public String getComment_count() {
        return comment_count;
    }

    public String getFavorite_count() {
        return favorite_count;
    }

    public String getBook_content() {
        return book_content;
    }

    public String getSold_kind() {
        return sold_kind;
    }

    public String getBook_id() {
        return book_id;
    }
}
