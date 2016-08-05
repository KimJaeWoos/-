package com.jwoos.android.sellbook.page1.grouplist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.base.retrofit.model.Comment;
import com.jwoos.android.sellbook.page1.ImagePager_Activity;
import com.jwoos.android.sellbook.page1.adapter.CommentAdapter;
import com.jwoos.android.sellbook.page1.userinfo.Userinfo_Activity;
import com.jwoos.android.sellbook.utils.ObjectUtils;
import com.jwoos.android.sellbook.utils.TimeFomat;
import com.jwoos.android.sellbook.widget.CommentEditText;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.jwoos.android.sellbook.widget.RialTextView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Grouplist_detail_Activity extends BaseActivity {

    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.toolbar_img)
    MLRoundedImageView iv_toolbar;
    @BindView(R.id.favorite)
    ImageView fa;
    @BindView(R.id.toolbar_nic)
    TextView tv_toolbar;
    @BindView(R.id.detail2_price)
    RialTextView tv_price;
    @BindView(R.id.book_name)
    TextView tv_book_name;
    @BindView(R.id.book_category)
    TextView tv_category;
    @BindView(R.id.book_content)
    TextView tv_book_content;
    @BindView(R.id.time)
    TextView tv_time;
    @BindView(R.id.img_indicator)
    Indicator indicater;
    @BindView(R.id.img_viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.soldout_view)
    TextView tv_soldout;
    @BindView(R.id.comment_view)
    CommentEditText et_comment;
    @BindView(R.id.btn_menu)
    LinearLayout btn_menu;
    @BindView(R.id.recyclerView_comment)
    RecyclerView rv_comment;
    @BindView(R.id.comment_add)
    TextView btn_comment;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.comment)
    RelativeLayout comment;



    private final int FAVORITE_OK = 1;
    private final int FAVORITE_CANCEL = 2;;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private String book_id;
    private String[] images;
    private boolean comment_flag;
    private List<Book_Info> items;
    private InputMethodManager mgr;
    private int item_position;
    private CommentAdapter ctAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist_detail);
        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setToolbar("");
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initialize();
        initialize_comment();
        detail_record();

        //댓글아이콘을 눌렀을경우
        if (comment_flag) {
            comment.performClick();
        }
    }


    private void initialize() {
        Intent extras = getIntent();
        book_id = extras.getStringExtra("book_id");
        item_position = extras.getIntExtra("item_position",0);
        comment_flag = extras.getBooleanExtra("comment_flag",false);

        ServiceGenerator.getService().getBook_detail(book_id, new retrofit.Callback<List<Book_Info>>() {
            @Override
            public void success(List<Book_Info> bookInfos, Response response) {
                items = bookInfos;
                setImage_url();
                setImage();

                //이미지 회전체크
                int book_rotate = Character.getNumericValue(bookInfos.get(0).getUser_profile_img_path().charAt(9));

                Picasso.with(getBaseContext())
                        .load(base_image_url_profile + bookInfos.get(0).getUser_profile_img_path())
                        .rotate(orientation(book_rotate))
                        .fit()
                        .into(iv_toolbar);
                tv_toolbar.setText(bookInfos.get(0).getUser_nik());
                tv_price.setText(bookInfos.get(0).getBook_price());
                tv_book_name.setText(bookInfos.get(0).getBook_name());
                tv_category.setText("카테고리 : " + bookInfos.get(0).getBook_category());
                tv_book_content.setText(bookInfos.get(0).getBook_content());

                //시간포맷
                try {
                    tv_time.setText(TimeFomat.detail_time(bookInfos.get(0).getBook_register_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //솔드아웃 셋팅
                if (bookInfos.get(0).getSold_kind().equals("YES")) {
                    tv_soldout.setVisibility(View.VISIBLE);
                }

                //북마크 셋팅
                if (bookInfos.get(0).getFavorite_chk().equals("OK")) {
                    fa.setTag(FAVORITE_OK);
                    fa.setBackgroundResource(R.drawable.ic_bookmark_white);

                } else {
                    fa.setTag(FAVORITE_CANCEL);
                    fa.setBackgroundResource(R.drawable.ic_bookmark_border);
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void initialize_comment() {
        rv_comment.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        //((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        rv_comment.setLayoutManager(mLayoutManager);

        ServiceGenerator.getService().get_comment(book_id, new retrofit.Callback<List<Comment>>() {
            @Override
            public void success(List<Comment> comments, Response response) {
                ctAdapter = new CommentAdapter(Grouplist_detail_Activity.this, comments, book_id);
                rv_comment.setAdapter(ctAdapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    private void set_favorite(String book_id, String flag) {
        ServiceGenerator.getService().set_favorite(book_id, flag, new retrofit.Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }


    private void setImage_url() {
        if (items.get(0).getBook_image2().length() < 1) {
            images = new String[]{items.get(0).getBook_image1()};
        } else if (items.get(0).getBook_image3().length() < 1) {
            images = new String[]{items.get(0).getBook_image1(), items.get(0).getBook_image2()};
        } else {
            images = new String[]{items.get(0).getBook_image1(), items.get(0).getBook_image2(), items.get(0).getBook_image3()};
        }
    }

    private void setImage() {
        indicatorViewPager = new IndicatorViewPager(indicater, viewPager);
        inflate = LayoutInflater.from(this);
        indicatorViewPager.setAdapter(adapter);
    }


    @OnClick({R.id.comment, R.id.send_msg, R.id.click_user, R.id.favorite, R.id.comment_add})
    public void click(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.comment:
                btn_menu.setVisibility(View.GONE);
                bottom.setVisibility(View.VISIBLE);

                et_comment.requestFocus();
                et_comment.setCursorVisible(true);
                mgr.showSoftInput(et_comment, InputMethodManager.SHOW_FORCED);

                sv.post(new Runnable() {
                    @Override
                    public void run() {
                        sv.scrollTo(0, rv_comment.getBottom());
                    }
                });

                break;

            case R.id.comment_add:
                if (!ObjectUtils.isEmpty(et_comment.getText().toString())) {
                    ServiceGenerator.getService().set_comment(book_id, et_comment.getText().toString(), "0", new retrofit.Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {
                            et_comment.setText("");
                            ctAdapter.commentSetting();
                            push_notification();

                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable()  {
                                public void run() {
                                    sv.scrollTo(0, rv_comment.getBottom());
                                }
                            }, 300);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            showToast("등록이 실패되었습니다");
                        }
                    });
                } else {
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(et_comment);
                }
                break;

            case R.id.send_msg:
                intent = new Intent(this, PopupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("phone", items.get(0).getBook_phone());
                intent.putExtra("price", items.get(0).getBook_price());
                intent.putExtra("name", items.get(0).getBook_name());

                break;

            case R.id.click_user:
                intent = new Intent(this, Userinfo_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("profile_flag", book_id);
                finish();
                break;

            case R.id.favorite:
                //final int cnt = Integer.parseInt(tv_favorite_cnt.getText().toString()); //처음 나타나는 카운트 갯수
                final int cnt = 1;
                if (v.getTag().equals(FAVORITE_OK)) {

                    v.setTag(FAVORITE_CANCEL);
                    fa.setBackgroundResource(R.drawable.ic_bookmark_border);
                    set_favorite(book_id, "CANCEL");
                } else if (v.getTag().equals(FAVORITE_CANCEL)) {
                    v.setTag(FAVORITE_OK);
                    fa.setBackgroundResource(R.drawable.ic_bookmark_white);
                    YoYo.with(Techniques.RotateIn)
                            .duration(200)
                            .playOn(fa);
                    set_favorite(book_id, "OK");
                }
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

    }

    private void push_notification() {
        ServiceGenerator.getService().fcm_push(book_id, new retrofit.Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        //북마크 상태 전달
        if (btn_menu.getVisibility() == View.GONE) {
            btn_menu.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
        } else {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("book_id",book_id);
            extra.putInt("item_position",item_position);
            extra.putString("item_flag",fa.getTag().toString());
            intent.putExtras(extra);
            setResult(Activity.RESULT_OK,intent);
            super.onBackPressed();

        }
        mgr.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        cancelToast();
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        cancelToast();
        super.onStop();

    }






    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_guide, container, false);
                if (images.length == 1) {
                    convertView.setVisibility(View.INVISIBLE);
                }
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.image, container, false);
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            int book_rotate = Character.getNumericValue(images[position].charAt(9));

            Picasso.with(getBaseContext())
                    .load(base_image_url_uploads + images[position])
                    .rotate(orientation(book_rotate))
                    .into((ImageView) convertView, new Callback() {
                        @Override
                        public void onSuccess() {
                            dimssDialog();
                        }

                        @Override
                        public void onError() {

                        }
                    });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ImagePager_Activity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("image1", items.get(0).getBook_image1());
                    intent.putExtra("image2", items.get(0).getBook_image2());
                    intent.putExtra("image3", items.get(0).getBook_image3());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return images.length;
        }
    };

    private void detail_record() {
        ServiceGenerator.getService().set_detail_record(book_id, new retrofit.Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }



}
