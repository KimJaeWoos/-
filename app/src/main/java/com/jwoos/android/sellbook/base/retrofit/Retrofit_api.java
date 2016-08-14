package com.jwoos.android.sellbook.base.retrofit;


import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.base.retrofit.model.Calling;
import com.jwoos.android.sellbook.base.retrofit.model.Comment;
import com.jwoos.android.sellbook.base.retrofit.model.Favorites;
import com.jwoos.android.sellbook.base.retrofit.model.Login;
import com.jwoos.android.sellbook.base.retrofit.model.NaverInfo;
import com.jwoos.android.sellbook.base.retrofit.model.Notice;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;


public interface Retrofit_api {

    @FormUrlEncoded
    @POST("/api/signup_chk.php")
    void Overlap_chk(@Field("info") String user_info,
                     @Field("num") String chk_num,
                     Callback<Login> cb);

    @FormUrlEncoded
    @POST("/api/signup.php")
    void Signup(@Field("user_id") String user_id,
                @Field("user_pwd") String user_pwd,
                @Field("user_email") String user_email,
                @Field("user_nic") String user_nic,
                @Field("user_major") String user_major,
                Callback<Login> cb);

    @FormUrlEncoded
    @POST("/api/login.php")
    void Login(@Field("user_id") String user_id,
               @Field("user_pwd") String email,
               Callback<Login> pm);

    @GET("/api/notice.php")
    void getNotice(Callback<List<Notice>> response);

    @GET("/api/qna.php")
    void getQna(Callback<List<Notice>> response);

    @Multipart
    @POST("/api/upload_File.php")
    void upload(@Part("myfile") TypedFile file,
                @Part("file_name") String file_name,
                Callback<String> cb);

    @Multipart
    @POST("/api/upload_profile.php")
    void upload_profile(@Part("myfile") TypedFile file,
                        @Part("file_name") String file_name,
                        Callback<String> cb);

    @FormUrlEncoded
    @POST("/api/register_book.php")
    void Create_board(@Field("book_name") String book_name,
                      @Field("book_category") String book_category,
                      @Field("book_price") String book_price,
                      @Field("book_content") String book_content,
                      @Field("book_image1") String book_image1,
                      @Field("book_image2") String book_image2,
                      @Field("book_image3") String book_image3,
                      @Field("book_image4") String book_image4,
                      @Field("book_image5") String book_image5,
                      Callback<Void> response);

    @GET("/api/my_info.php")
    void getMy_info(@Query("item_start") int item_start,
                    @Query("item_end") int item_end,
                    Callback<List<Book_Info>> response);

    @GET("/api/user_info.php")
    void getUser_info(@Query("item_start") int item_start,
                      @Query("item_end") int item_end,
                      @Query("book_id") String book_id,
                      Callback<List<Book_Info>> response);

    @GET("/api/my_info_detail.php")
    void getInfo_detail(@Query("book_id") String book_id,
                        Callback<List<Book_Info>> response);

    @GET("/api/book_list_all.php")
    void getBook_list(@Query("item_start") int item_start,
                      @Query("item_end") int item_end,
                      Callback<List<Book_Info>> response);

    @GET("/api/book_list_search.php")
    void getBook_search(@Query("item_start") int item_start,
                        @Query("item_end") int item_end,
                        @Query("keyword") String keyword,
                        @Query("category") String category,
                        Callback<List<Book_Info>> response);

    @GET("/api/book_list_detail.php")
    void getBook_detail(@Query("book_id") String book_id,
                        Callback<List<Book_Info>> response);

    @FormUrlEncoded
    @POST("/api/delete_item.php")
    void delete_item(@Field("book_id") String book_id,
                     Callback<Void> response);

    @FormUrlEncoded
    @POST("/api/change_item.php")
    void change_item(@Field("book_id") String book_id,
                     @Field("sold_kind") String sold_kind,
                     Callback<String> response);

    @GET("/api/set_favorite.php")
    void set_favorite(@Query("book_id") String book_id,
                      @Query("flag") String flag,
                      Callback<Void> response);

    @GET("/api/get_favorite_item.php")
    void get_favorite_item(Callback<List<Favorites>> response);

    @GET("/api/set_phone.php")
    void set_userPhone(@Query("user_phone") String user_phone,
                       Callback<String> response);

    @GET("/api/get_position_item.php")
    void get_position_item(@Query("book_id") String book_id,
                           Callback<List<Book_Info>> response);

    @FormUrlEncoded
    @POST("/api/set_comment.php")
    void set_comment(@Field("book_id") String book_id,
                     @Field("book_comment") String book_comment,
                     @Field("comment_id") String comment_id,
                     Callback<Void> response);

    @GET("/api/get_comment.php")
    void get_comment(@Query("book_id") String book_id,
                     Callback<List<Comment>> response);

    @FormUrlEncoded
    @POST("/api/set_detail_record.php")
    void set_detail_record(@Field("book_id") String book_id,
                           Callback<Void> response);

    @FormUrlEncoded
    @POST("/fcm/register.php")
    void registerToken(@Field("Token") String token,
                       Callback<Void> response);

    @FormUrlEncoded
    @POST("/api/fcm_register.php")
    void set_fcm(@Field("Token") String token,
                 Callback<Void> response);

    @FormUrlEncoded
    @POST("/fcm/push_notification.php")
    void fcm_push(@Field("book_id") String book_id,
                  Callback<Void> response);

    @FormUrlEncoded
    @POST("/api/delete_comment.php")
    void delete_comment(@Field("comment_id") String comment_id,
                        Callback<Void> response);

    @GET("/api/delete_fcm.php")
    void delete_fcm(Callback<Void> response);

    @FormUrlEncoded
    @POST("/api/calling_info.php")
    void get_record(@Field("user_phone") String phoneNum,
                    Callback<List<Calling>> response);

    @GET("/")
    void getIsbn(@Query("d_isbn") String d_isbn,
                 @Query("query") String query,
                 Callback<NaverInfo> response);
}
