package com.jwoos.android.sellbook.page1.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.grouplist.Grouplist_detail_Activity;
import com.jwoos.android.sellbook.page1.myinfo.Myinfo_detail_Activity;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.utils.ObjectUtils;
import com.jwoos.android.sellbook.utils.TimeFomat;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<Book_Info> bookInfos;
    private Activity mContext;
    private String base_image_url_uploads;
    private String base_image_url_profile;
    private final static int FAVORITE_OK = 1;
    private final static int FAVORITE_CANCEL = 2;
    private final static int REQ_CODE = 5000;
    private final static int REQ_CODE2 = 2500;
    private int item_position;
    private String book_id;

    public GroupAdapter(Activity context, List<Book_Info> objects) {
        this.mContext = (Activity)context;
        this.bookInfos = objects;
        this.base_image_url_uploads = Gloval.getBase_image_url_uploads();
        this.base_image_url_profile = Gloval.getBase_image_url_profile();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            viewHolder.bind(i);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return bookInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.card_view)
        CardView cardView;
        @Nullable
        @BindView(R.id.card_profile)
        MLRoundedImageView iv_profile;
        @Nullable
        @BindView(R.id.card_nic)
        TextView tv_username;
        @Nullable
        @BindView(R.id.card_time)
        TextView tv_time;
        @Nullable
        @BindView(R.id.selected_photos_container)
        ViewGroup mSelectedImagesContainer;
        @Nullable
        @BindView(R.id.book_name)
        TextView tv_bookname;
        @Nullable
        @BindView(R.id.book_price)
        TextView tv_bookprice;
        @Nullable
        @BindView(R.id.book_content)
        TextView tv_bookcontent;
        @Nullable
        @BindView(R.id.soldout_view)
        TextView tv_soldout;
        @Nullable
        @BindView(R.id.favorite)
        ImageView iv_favorite;
        @Nullable
        @BindView(R.id.comment)
        ImageView iv_comment;
        @Nullable
        @BindView(R.id.favorite_cnt)
        TextView tv_favorite_cnt;
        @Nullable
        @BindView(R.id.comment_cnt)
        TextView tv_comment_cnt;
        @Nullable
        @BindView(R.id.maxLine_text)
        TextView max_text;

        private String[] image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(int position) throws ParseException {
            //프로필 사진 회전값
            int profile_rotate = Character.getNumericValue(bookInfos.get(position).getUser_profile_img_path().charAt(9));
            Picasso.with(mContext)
                    .load(base_image_url_profile + bookInfos.get(position).getUser_profile_img_path())
                    .fit()
                    .rotate(orientation(profile_rotate))
                    .into(iv_profile);
            tv_username.setText(bookInfos.get(position).getUser_nik());

            //시간포멧
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = format.parse(bookInfos.get(position).getBook_register_time());
            String time = TimeFomat.list_time(date);
            tv_time.setText(time);

            tv_bookname.setText("책 제목 : " + bookInfos.get(position).getBook_name());
            tv_bookcontent.setText(bookInfos.get(position).getBook_content());
            tv_bookprice.setText(bookInfos.get(position).getBook_price());
            tv_comment_cnt.setText(bookInfos.get(position).getComment_count());
            tv_favorite_cnt.setText(bookInfos.get(position).getFavorite_count());

            //내용이 1줄이상일 경우
            tv_bookcontent.post(new Runnable() {
                @Override
                public void run() {
                    if (tv_bookcontent.getLineCount() > 1 ) {
                        max_text.setVisibility(View.VISIBLE);
                    } else {
                        max_text.setVisibility(View.GONE);
                    }
                }
            });


            //판매완료 확인
            if (bookInfos.get(position).getSold_kind().equals("YES")) {
                tv_soldout.setVisibility(View.VISIBLE);
            } else {
                tv_soldout.setVisibility(View.INVISIBLE);
            }

            //이미지 셋팅
            imageSetting();
            image = new String[] {bookInfos.get(position).getBook_image1(), bookInfos.get(position).getBook_image2(), bookInfos.get(position).getBook_image3(),
                                    bookInfos.get(position).getBook_image4(), bookInfos.get(position).getBook_image5()};
            showMedia(image);

            //자신이 올린 게시물은 즐켜찾기 버튼 비활성화
            if(bookInfos.get(position).getUser_chk().equals("NO")) {
                if (bookInfos.get(position).getFavorite_count().equals("0")) {
                    tv_favorite_cnt.setVisibility(View.INVISIBLE);
                } else {
                    tv_favorite_cnt.setVisibility(View.VISIBLE);
                }
                click(position, bookInfos.get(position).getBook_id(), bookInfos.get(position).getFavorite_chk());
            } else {
                iv_favorite.setClickable(false);
                tv_favorite_cnt.setVisibility(View.VISIBLE);
            }

            detail_click(position, bookInfos.get(position).getUser_chk());
        }

        private void imageSetting() {
        }

        private void detail_click(final int position, final String flag) {
            assert cardView != null;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (flag.equals("OK")){
                        intent = new Intent(mContext, Myinfo_detail_Activity.class);
                        intent.putExtra("book_id", bookInfos.get(position).getBook_id());
                        intent.putExtra("item_position",position);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivityForResult(intent, REQ_CODE2);

                    } else if (flag.equals("NO")) {
                        intent = new Intent(mContext, Grouplist_detail_Activity.class);
                        intent.putExtra("book_id", bookInfos.get(position).getBook_id());
                        intent.putExtra("item_position",position);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivityForResult(intent, REQ_CODE);

                    }
                    mContext.overridePendingTransition(0,0);
                }
            });

            assert iv_comment != null;
            iv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (flag.equals("OK")){
                        intent = new Intent(mContext, Myinfo_detail_Activity.class);
                        intent.putExtra("book_id", bookInfos.get(position).getBook_id());
                        intent.putExtra("item_position",position);
                        intent.putExtra("comment_flag",true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivityForResult(intent, REQ_CODE2);

                    } else if (flag.equals("NO")) {
                        intent = new Intent(mContext, Grouplist_detail_Activity.class);
                        intent.putExtra("book_id", bookInfos.get(position).getBook_id());
                        intent.putExtra("item_position",position);
                        intent.putExtra("comment_flag",true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivityForResult(intent, REQ_CODE);

                    }
                    mContext.overridePendingTransition(0,0);
                }
            });
        }


        private void click(final int position, final String book_id, final String favorite_chk) {
            if (favorite_chk.equals("OK")) {
                iv_favorite.setTag(FAVORITE_OK);
                iv_favorite.setBackgroundResource(R.drawable.ic_bookmark);
            } else {
                iv_favorite.setTag(FAVORITE_CANCEL);
                iv_favorite.setBackgroundResource(R.drawable.ic_bookmark_grey);
            }

            iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //final int cnt = Integer.parseInt(favorite_chk); //처음 나타나는 카운트 갯수
                    if (v.getTag().equals(FAVORITE_OK)) {
                        int cnt = Integer.parseInt(tv_favorite_cnt.getText().toString());
                        cnt = cnt - 1;
                        v.setTag(FAVORITE_CANCEL);
                        if ( cnt > 0) {
                            tv_favorite_cnt.setVisibility(View.VISIBLE);
                        } else {
                            tv_favorite_cnt.setVisibility(View.INVISIBLE);
                        }
                        tv_favorite_cnt.setText(String.valueOf(cnt));
                        YoYo.with(Techniques.BounceInDown)
                                .duration(300)
                                .playOn(tv_favorite_cnt);
                        iv_favorite.setBackgroundResource(R.drawable.ic_bookmark_grey);
                        set_favorite(book_id, "CANCEL");
                    } else if (v.getTag().equals(FAVORITE_CANCEL)) {
                        int cnt = Integer.parseInt(tv_favorite_cnt.getText().toString());
                        cnt = cnt + 1;
                        iv_favorite.setBackgroundResource(R.drawable.ic_bookmark);
                        tv_favorite_cnt.setVisibility(View.VISIBLE);
                        tv_favorite_cnt.setText(String.valueOf(cnt));
                        v.setTag(FAVORITE_OK);
                        YoYo.with(Techniques.RotateIn)
                                .duration(200)
                                .playOn(iv_favorite);

                        YoYo.with(Techniques.BounceInUp)
                                .duration(300)
                                .playOn(tv_favorite_cnt);

                        set_favorite(book_id, "OK");
                    }
                }
            });


        }

        private void set_favorite(String book_id, String flag) {
            ServiceGenerator.getService().set_favorite(book_id, flag, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }

        public void showMedia(String[] image) {
            mSelectedImagesContainer.removeAllViews();
            mSelectedImagesContainer.setVisibility(View.VISIBLE);

            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, itemView.getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, itemView.getResources().getDisplayMetrics());

            for (String uri : image) {
                if (!ObjectUtils.isEmpty(uri.toString())) {
                    View imageHolder = LayoutInflater.from(itemView.getContext()).inflate(R.layout.page1_image_item, null);
                    ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

                    int book_rotate = 0;
                    if (!uri.isEmpty()) book_rotate = Character.getNumericValue(uri.charAt(9));

                    Picasso.with(mContext)
                            .load(base_image_url_uploads + uri)
                            .rotate(orientation(book_rotate))
                            .into(thumbnail);
                    mSelectedImagesContainer.addView(imageHolder);
                    thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
                }
            }
        }

    }
    //디테일 액티비티에서 돌아왔을때 즐겨찾기 상태확인
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE) {
                item_position = data.getIntExtra("item_position", 0);
                book_id = data.getStringExtra("book_id");
                ServiceGenerator.getService().get_position_item(book_id, new Callback<List<Book_Info>>() {
                    @Override
                    public void success(List<Book_Info> book_info, Response response) {
                        String ct_count = book_info.get(0).getComment_count();
                        String fa_count = book_info.get(0).getFavorite_count();
                        String fa_chk = book_info.get(0).getFavorite_chk();
                        bookInfos.get(item_position).setComment_count(ct_count);
                        bookInfos.get(item_position).setFavorite_chk(fa_chk);
                        bookInfos.get(item_position).setFavorite_count(fa_count);
                        notifyItemChanged(item_position);

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            } else if (requestCode == REQ_CODE2) {
                item_position = data.getIntExtra("item_position", 0);
                book_id = data.getStringExtra("book_id");
                Boolean remove_flag = data.getBooleanExtra("remove_flag", false);
                if (remove_flag) {
                    bookInfos.remove(item_position);
                    notifyItemRemoved(item_position);
                    notifyItemRangeChanged(item_position, getItemCount());
                } else {
                    ServiceGenerator.getService().get_position_item(book_id, new Callback<List<Book_Info>>() {
                        @Override
                        public void success(List<Book_Info> book_info, Response response) {
                            String ct_count = book_info.get(0).getComment_count();
                            String fa_count = book_info.get(0).getFavorite_count();
                            String fa_chk = book_info.get(0).getFavorite_chk();
                            String sold_out = book_info.get(0).getSold_kind();
                            bookInfos.get(item_position).setComment_count(ct_count);
                            bookInfos.get(item_position).setFavorite_chk(fa_chk);
                            bookInfos.get(item_position).setFavorite_count(fa_count);
                            bookInfos.get(item_position).setSold_kind(sold_out);
                            notifyItemChanged(item_position);

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }
        }

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
}
