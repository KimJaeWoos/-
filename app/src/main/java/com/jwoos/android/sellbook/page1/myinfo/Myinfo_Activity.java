package com.jwoos.android.sellbook.page1.myinfo;


import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.adapter.GridAdapter_MyInfo;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.mime.TypedFile;

/**
 * Created by Jwoo on 2016-06-11.
 */

public class Myinfo_Activity extends BaseActivity {

    @BindView(R.id.backdrop)
    ImageView iv_back;
    @BindView(R.id.profile)
    MLRoundedImageView iv_profile;
    @BindView(R.id.username)
    TextView tv_name;
    @BindView(R.id.sell_count)
    TextView tv_sell_count;
    @BindView(R.id.soldout_count)
    TextView tv_sold_count;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searh_empty)
    RelativeLayout empty_msg;

    GridAdapter_MyInfo mAdapter;

    private final static int REQ_CODE = 4321;

    private GridLayoutManager lLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        setToolbar("");
        initialize();
        getData(0, 30);

    }

    private void initialize() {
        lLayout = new GridLayoutManager(this, 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lLayout);

    }

    @OnClick(R.id.profile)
    public void imageClick(View v) {
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(Myinfo_Activity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        Log.d("ted", "uri: " + uri);
                        Log.d("ted", "uri.getPath(): " + uri.getPath());
                        File file = null;
                        if (uri.toString().substring(0,4).equals("file")) {
                            file = new File(uri.getPath());
                        } else {
                            file = new File(getPath(uri));
                        }
                        Date date = new Date();

                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(uri.getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                        String register_time = String.valueOf(date.getTime());
                        String image_uri = "_" + orientation + "_" + register_time + "_" + file.getName();
                        uploadFile(file, image_uri);


                    }
                })
                .setTitle("프로필 사진 설정")
                .setPeekHeight(getResources().getDisplayMetrics().heightPixels / 2)
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());

    }


    private void getData(int item_start, final int item_end) {
        ServiceGenerator.getService().getMy_info(item_start, item_end, new Callback<List<Book_Info>>() {

                    @Override
                    public void success(final List<Book_Info> bookInfos, Response response) {
                        Log.d("Retrofit", "Retrofit: Success");
                        int profile_rotate = Character.getNumericValue(bookInfos.get(0).getUser_profile_img_path().charAt(9));

                        //이미지, 닉네임 셋팅
                        tv_name.setText(bookInfos.get(0).getUser_nik());
                        Picasso.with(getBaseContext())
                                .load(base_image_url_profile + bookInfos.get(0).getUser_profile_img_path())
                                .transform(new BlurTransformation(getBaseContext(), 25, 2))
                                .fit()
                                .rotate(orientation(profile_rotate))
                                .into(iv_back);

                        Picasso.with(getBaseContext())
                                .load(base_image_url_profile + bookInfos.get(0).getUser_profile_img_path())
                                .fit()
                                .rotate(orientation(profile_rotate))
                                .into(iv_profile);

                        //타이틀 스크롤반응
                        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                            boolean isShow = false;
                            int scrollRange = -1;

                            @Override
                            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                                if (scrollRange == -1) {
                                    scrollRange = appBarLayout.getTotalScrollRange();
                                }
                                if (scrollRange + verticalOffset == 0) {
                                    collapsingToolbarLayout.setTitle(bookInfos.get(0).getUser_nik());
                                    isShow = true;
                                } else if (isShow) {
                                    collapsingToolbarLayout.setTitle("");
                                    isShow = false;
                                }
                            }
                        });

                        //데이터가 비어있는지 확인
                        if (bookInfos.get(0).getEmpty_chk().equals("true")) {
                            mAdapter = new GridAdapter_MyInfo(Myinfo_Activity.this, bookInfos, true);
                            mRecyclerView.setAdapter(mAdapter);

                            //판매중 판매완료 카운트
                            if (String.valueOf(bookInfos.size()) == bookInfos.get(0).getSell_count()) {
                                tv_sell_count.setText(bookInfos.get(0).getSell_count());
                                tv_sold_count.setText(0);
                            } else {
                                int count = bookInfos.size() - Integer.parseInt(bookInfos.get(0).getSell_count());
                                tv_sell_count.setText(bookInfos.get(0).getSell_count());
                                tv_sold_count.setText(String.valueOf(count));
                            }
                        } else {
                            empty_msg.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("TAG", error.getMessage());
                    }
                }

        );
    }

    public void clickDetail(String book_id, int position) {
        Intent intent = null;
        intent = new Intent(Myinfo_Activity.this, Myinfo_detail_Activity.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("item_position", position);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQ_CODE);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE:
                final String sold_kind = data.getStringExtra("sold_kind");
                final Boolean remove_flag = data.getBooleanExtra("remove_flag", false);
                final int item_position = data.getIntExtra("item_position", 0);
                Dlog.d(sold_kind);
                //수정필요요
                ServiceGenerator.getService().getMy_info(0, 30, new Callback<List<Book_Info>>() {
                    @Override
                    public void success(final List<Book_Info> book_infos, Response response) {
                        //판매중 판매완료 카운트
                        if (book_infos.get(0).getEmpty_chk().equals("false")) {
                            tv_sell_count.setText("-");
                            tv_sold_count.setText("-");
                        } else if (String.valueOf(book_infos.size()) == book_infos.get(0).getSell_count()) {
                            tv_sell_count.setText(book_infos.get(0).getSell_count());
                            tv_sold_count.setText(0);
                        } else {
                            int count = book_infos.size() - Integer.parseInt(book_infos.get(0).getSell_count());
                            tv_sell_count.setText(book_infos.get(0).getSell_count());
                            tv_sold_count.setText(String.valueOf(count));
                        }

                        //타이틀 스크롤반응
                        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                            boolean isShow = false;
                            int scrollRange = -1;

                            @Override
                            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                                if (scrollRange == -1) {
                                    scrollRange = appBarLayout.getTotalScrollRange();
                                }
                                if (scrollRange + verticalOffset == 0) {
                                    collapsingToolbarLayout.setTitle(book_infos.get(0).getUser_nik());
                                    isShow = true;
                                } else if (isShow) {
                                    collapsingToolbarLayout.setTitle("");
                                    isShow = false;
                                }
                            }
                        });

                        mAdapter.resetData(sold_kind, remove_flag, item_position);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Dlog.e(error.getMessage());
                    }
                });

                break;
        }
    }

    //절대경로얻기
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //파일 업로드
    private void uploadFile(File file, String file_name) {

        TypedFile typedFile = new TypedFile("multipart/form-data", file);

        ServiceGenerator.getService().upload_profile(typedFile, file_name, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d("Retrofit", "Upload success");
                String image_name = s.toString();
                int profile_rotate = Character.getNumericValue(image_name.charAt(9));

                Picasso.with(getBaseContext())
                        .load(base_image_url_profile + image_name)
                        .transform(new BlurTransformation(getBaseContext(), 25, 1))
                        .fit()
                        .rotate(orientation(profile_rotate))
                        .into(iv_back);
                Picasso.with(getBaseContext())
                        .load(base_image_url_profile + image_name)
                        .fit()
                        .rotate(orientation(profile_rotate))
                        .into(iv_profile);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit", "Upload" + error.getMessage());
                showToast("다시시도해주세요 :)");

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dimssDialog();
    }
}
