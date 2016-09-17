package com.jwoos.android.sellbook.page1.userinfo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.adapter.GridAdapter;
import com.jwoos.android.sellbook.page1.adapter.GridAdapter_UserInfo;
import com.jwoos.android.sellbook.page1.adapter.RecyclerItemClickListener;
import com.jwoos.android.sellbook.page1.grouplist.Grouplist_detail_Activity;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jwoo on 2016-06-11.
 */

public class Userinfo_Activity extends BaseActivity {

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

    GridAdapter_UserInfo mAdapter;

    private GridLayoutManager lLayout;
    private List<Book_Info> items;
    private boolean first_count;
    private String book_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        setToolbar("");
        initialize();
        getData(0, 30);

    }

    private void initialize() {
        Intent extras = getIntent();
        book_id = extras.getStringExtra("profile_flag");
        first_count = true;
        lLayout = new GridLayoutManager(this, 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lLayout);

    }


    private void getData(int item_start, int item_end) {
        ServiceGenerator.getService().getUser_info(item_start, item_end, book_id, new Callback<List<Book_Info>>() {

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
                    mAdapter = new GridAdapter_UserInfo(Userinfo_Activity.this, bookInfos, false);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = null;
                                    intent = new Intent(getBaseContext(), Grouplist_detail_Activity.class);
                                    intent.putExtra("book_id", bookInfos.get(position).getBook_id());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);

                                }
                            }));


                    //판매중 판매완료 카운트
                    if (String.valueOf(bookInfos.size()) == bookInfos.get(0).getSell_count()) {
                        tv_sell_count.setText(bookInfos.get(0).getSell_count());
                        tv_sold_count.setText(0);
                    } else {
                        int count = bookInfos.size() - Integer.parseInt(bookInfos.get(0).getSell_count());
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
