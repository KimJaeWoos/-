package com.jwoos.android.sellbook.page1.myinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.utils.ObjectUtils;
import com.jwoos.android.sellbook.utils.TimeFomat;
import com.jwoos.android.sellbook.widget.CommentEditText;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.jwoos.android.sellbook.widget.RialTextView;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jwoo on 2016-07-06.
 */
public class Myinfo_detail_Activity extends BaseActivity {

    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.detail2_price)
    RialTextView tv_price;
    @BindView(R.id.user_img)
    MLRoundedImageView iv_user;
    @BindView(R.id.user_nic)
    TextView tv_nic;
    @BindView(R.id.book_condition)
    TextView tv_condition;
    @BindView(R.id.book_category)
    TextView tv_category;
    @BindView(R.id.book_description)
    TextView tv_description;
    @BindView(R.id.book_board)
    TextView tv_content;
    @BindView(R.id.img_indicator)
    Indicator indicater;
    @BindView(R.id.img_viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.soldout_view)
    TextView tv_soldout;
    @BindView(R.id.recyclerView_comment)
    RecyclerView rv_comment;
    @BindView(R.id.comment_add)
    TextView btn_comment;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.comment_view)
    CommentEditText et_comment;
    @BindView(R.id.btn_menu)
    LinearLayout btn_menu;
    @BindView(R.id.comment)
    RelativeLayout comment;
    @BindView(R.id.after_price)
    TextView tv_after_price;


    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private String book_id, sold_kind;
    private CommentAdapter ctAdapter;
    private int item_position;
    private InputMethodManager mgr;
    private boolean remove_flag = false;
    private String state;
    private List<Book_Info> items;

    String[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_detail);
        this.setToolbar("");
        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getData();
        initialize_comment();

    }

    private void getData() {
        Intent extras = getIntent();
        book_id = extras.getStringExtra("book_id");
        item_position = extras.getIntExtra("item_position", 0);

        ServiceGenerator.getService().getInfo_detail(book_id, new Callback<List<Book_Info>>() {
            @Override
            public void success(List<Book_Info> bookInfos, Response response) {
                items = bookInfos;
                sold_kind = bookInfos.get(0).getSold_kind();
                state = bookInfos.get(0).getSold_kind();
                setImage_url();
                setImage();

                int book_rotate = Character.getNumericValue(bookInfos.get(0).getUser_profile_img_path().charAt(9));
                Picasso.with(getBaseContext())
                        .load(base_image_url_profile + bookInfos.get(0).getUser_profile_img_path())
                        .rotate(orientation(book_rotate))
                        .fit()
                        .into(iv_user);
                tv_nic.setText(bookInfos.get(0).getUser_nik());
                tv_price.setText(bookInfos.get(0).getBook_sellprice());
                tv_condition.setText(bookInfos.get(0).getBook_condition());
                tv_category.setText(bookInfos.get(0).getBook_category());
                tv_description.setText(bookInfos.get(0).getBook_description());
                tv_content.setText(bookInfos.get(0).getBook_content());
                tv_after_price.setText(bookInfos.get(0).getBook_price());
                tv_after_price.setPaintFlags(tv_after_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                if (sold_kind.equals("YES")) {
                    tv_soldout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("retrofit", error.getMessage());
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
                ctAdapter = new CommentAdapter(Myinfo_detail_Activity.this, comments, book_id);
                rv_comment.setAdapter(ctAdapter);
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


    @OnClick({R.id.sold_chk, R.id.item_delete, R.id.comment, R.id.comment_add, R.id.condition_guide})
    public void click(View v) {
        Dialog.Builder builder = null;

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

                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
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
            case R.id.condition_guide:

                break;

            case R.id.sold_chk:
                builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        String value = String.valueOf(getSelectedValue());

                        if (value.equals("판매중")) {
                            if (sold_kind.equals("YES")) {
                                setSold_kind(book_id, "NO", "판매중");
                            } else {
                                //상태 그대로
                            }
                        } else if (value.equals("판매완료")) {
                            if (sold_kind.equals("NO")) {
                                setSold_kind(book_id, "YES", "판매완료");
                            } else {
                                //상태 그대로
                            }
                        }
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                ((SimpleDialog.Builder) builder).items(new String[]{"판매중", "판매완료"}, 0)
                        .title("변경하기")
                        .positiveAction("확인")
                        .negativeAction("취소");
                break;

            case R.id.item_delete:
                builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        ServiceGenerator.getService().delete_item(book_id, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                                showToast("삭제되었습니다");
                                remove_flag = true;
                                onBackPressed();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                showToast("다시시도해주세요");
                                remove_flag = true;
                                onBackPressed();
                                Log.d("error", error.getMessage());
                            }
                        });
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                String colorText = "<font color='black'>판매글을 정말 삭제하시겠습니까?<font>";
                ((SimpleDialog.Builder) builder).message(Html.fromHtml(colorText))
                        .positiveAction("확인")
                        .negativeAction("취소");
                break;
        }

        if (builder != null) {
            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.setCancelable(false);
            fragment.show(getSupportFragmentManager(), null);
        }

    }

    private void setSold_kind(String book_id, String yes, String sta) {
        state = yes;
        final String satate2 = sta;

        ServiceGenerator.getService().change_item(book_id, yes, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                showToast("상태가 '" + satate2 + "' 로 변경되었습니다");

                sold_kind = s.toString();
                if (s.toString().equals("YES")) {
                    tv_soldout.setVisibility(View.VISIBLE);
                } else {
                    tv_soldout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_guide, container, false);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (position == 0) {
                Dlog.d("커버추가사진");
                convertView = inflate.inflate(R.layout.image_cover, container, false);
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                Dlog.d("사용자추가사진");
                convertView = inflate.inflate(R.layout.image, container, false);
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            //페이지 셋팅
            if (position == 0) {
                ImageView iv_cover = (ImageView) convertView.findViewById(R.id.book_cover);
                final TextView tv_name = (TextView) convertView.findViewById(R.id.book_name);
                TextView tv_author = (TextView) convertView.findViewById(R.id.book_author);
                TextView tv_publisher = (TextView) convertView.findViewById(R.id.book_publisher);

                Picasso.with(getBaseContext())
                        .load(items.get(0).getBook_cover())
                        .into((ImageView) iv_cover);
                tv_name.setText(items.get(0).getBook_name());
                tv_author.setText(items.get(0).getBook_author());
                tv_publisher.setText(items.get(0).getBook_publisher() + "|"
                        + items.get(0).getBook_pubDate().substring(0, 4));

                tv_name.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            //누를떄,움직일떄
                            case MotionEvent.ACTION_DOWN:
                            case MotionEvent.ACTION_MOVE:
                                tv_name.setSelected(true);
                                break;
                            //땟을때
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                tv_name.setSelected(false);
                                break;
                        }
                        return false;
                    }
                });
            } else {
                int book_rotate = Character.getNumericValue(images[position - 1].charAt(9));
                Picasso.with(getBaseContext())
                        .load(base_image_url_uploads + images[position - 1])
                        .rotate(orientation(book_rotate))
                        .into((ImageView) convertView);

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
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return images.length + 1;
        }

        private int orientation(int orientation) {

            int rotate = 0;

            if (orientation == 2)
                rotate = 0;
            else if (orientation == 3)
                rotate = 180;
            else if (orientation == 4)
                rotate = 180;
            else if (orientation == 5)
                rotate = 90;
            else if (orientation == 6)
                rotate = 90;
            else if (orientation == 7)
                rotate = -90;
            else if (orientation == 8)
                rotate = -90;

            return rotate;
        }
    };

    @Override
    public void onBackPressed() {
        if (btn_menu.getVisibility() == View.GONE) {
            btn_menu.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
        } else {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("sold_kind", state);
            extra.putString("book_id", book_id);
            extra.putInt("item_position", item_position);
            extra.putBoolean("remove_flag", remove_flag);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        mgr.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);

    }

    protected void setToolbar(String title) {
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
