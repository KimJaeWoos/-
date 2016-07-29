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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.adapter.GridAdapter;
import com.jwoos.android.sellbook.page1.adapter.RecyclerItemClickListener;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by Jwoo on 2016-06-11.
 */

public class Myinfo_Activity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

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

    GridAdapter mAdapter;



    private final int PICK_PHOTO = 1;
    private final int CAMERA_PHOTO = 2;
    private final static int REQ_CODE = 1300;

    private MaterialDialog alert;
    private GridLayoutManager lLayout;
    private List<Book_Info> items;
    private boolean first_count;
    private String data_empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        setToolbar("");
        initialize();
        getData(0, 30);

    }

    private void initialize() {
        first_count = true;
        iv_profile.setOnClickListener(this);
        lLayout = new GridLayoutManager(this, 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lLayout);

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
                data_empty = bookInfos.get(0).getEmpty_chk();
                if (data_empty.equals("true")) {
                    if (first_count == true) {
                        first_count = false;
                        items = new ArrayList<>();
                        mAdapter = new GridAdapter(Myinfo_Activity.this, items, true);
                        mRecyclerView.setAdapter(mAdapter);
                        for (int i = 0; i < bookInfos.size(); i++) {
                            items.add(bookInfos.get(i));
                        }
                    }

                    //판매중 판매완료 카운트
                    if(String.valueOf(bookInfos.size()) == bookInfos.get(0).getSell_count()){
                        tv_sell_count.setText(bookInfos.get(0).getSell_count());
                        tv_sold_count.setText(0);
                    } else {
                        int count = bookInfos.size()-Integer.parseInt(bookInfos.get(0).getSell_count());
                        tv_sell_count.setText(bookInfos.get(0).getSell_count());
                        tv_sold_count.setText(String.valueOf(count));
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("TAG", error.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(Myinfo_Activity.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("사진 촬영");
        arrayAdapter.add("사진 선택");

        ListView listView = new ListView(getBaseContext());
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

        alert = new MaterialDialog(Myinfo_Activity.this).setContentView(listView);

        alert.setPositiveButton("취소", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_PHOTO);
            } catch (Exception e) {

            }

        } else if (position == 1) {
            try {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PICK_PHOTO);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (data == null) return;
                Uri uri = data.getData();
                String image_uri = getPath(uri);
                File file = new File(image_uri);
                Date date = new Date();

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                String register_time = String.valueOf(date.getTime());
                image_uri = "_" + orientation + "_" + register_time + "_" + file.getName();
                uploadFile(file, image_uri);
                break;

            case CAMERA_PHOTO:
                //bitmap = data.getParcelableExtra("data");
                //if(bitmap == null) return;
                break;

            case REQ_CODE:
                if (requestCode == REQ_CODE) {

                    String sold_kind  = data.getStringExtra("sold_kind");
                    Boolean remove_flag = data.getBooleanExtra("remove_flag", false);
                    int item_position = data.getIntExtra("item_position",0);

                    //수정필요요
                   ServiceGenerator.getService().getMy_info(0, 30, new Callback<List<Book_Info>>() {
                        @Override
                        public void success(List<Book_Info> book_infos, Response response) {
                            //판매중 판매완료 카운트
                            if(book_infos.get(0).getEmpty_chk().equals("false")) {
                                tv_sell_count.setText("-");
                                tv_sold_count.setText("-");
                            }
                            else if(String.valueOf(book_infos.size()) == book_infos.get(0).getSell_count()){
                                tv_sell_count.setText(book_infos.get(0).getSell_count());
                                tv_sold_count.setText(0);
                            } else {
                                int count = book_infos.size()-Integer.parseInt(book_infos.get(0).getSell_count());
                                tv_sell_count.setText(book_infos.get(0).getSell_count());
                                tv_sold_count.setText(String.valueOf(count));
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                    mAdapter.resetData(sold_kind, remove_flag, item_position);
                }
                break;
        }

    }

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
                alert.dismiss();
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
                alert.dismiss();
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
