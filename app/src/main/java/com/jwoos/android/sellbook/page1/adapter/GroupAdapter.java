package com.jwoos.android.sellbook.page1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.grouplist.Grouplist_detail_Activity;
import com.jwoos.android.sellbook.utils.Dlog;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by Jwoo on 2016-08-08.
 */
public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private List<Book_Info> book_infos;

    //생성자
    public GroupAdapter(Activity context, List<Book_Info> list) {
        this.context = context;
        this.book_infos = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //카드 아이템을 인플레이트
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_list2, parent, false);
        return new ViewHolder_Body(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder_Body) {
            final ViewHolder_Body body_holder = (ViewHolder_Body) holder;

            body_holder.tv_title.setText(book_infos.get(position).getBook_name());
            body_holder.tv_publisher.setText(book_infos.get(position).getBook_author() + "|" + book_infos.get(position).getBook_publisher());
            body_holder.tv_date.setText(book_infos.get(position).getBook_pubDate());
            body_holder.tv_category.setText(book_infos.get(position).getBook_category());
            body_holder.tv_condition.setText(book_infos.get(position).getBook_condition());
            body_holder.tv_nic.setText(book_infos.get(position).getUser_nik());
            body_holder.tv_before_price.setText(book_infos.get(position).getBook_price());
            body_holder.tv_after_price.setText(book_infos.get(position).getBook_sellprice());
            body_holder.tv_before_price.setPaintFlags(body_holder.tv_before_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            Picasso.with(context)
                    .load(book_infos.get(position).getBook_cover())
                    .fit()
                    .into(body_holder.iv_cover);

            String discount = getDiscount(book_infos.get(position).getBook_price(), book_infos.get(position).getBook_sellprice());
            body_holder.tv_discount.setText("[" + discount + "%↓]");

            assert body_holder.btn_click != null;
            body_holder.btn_click.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        //누를떄,움직일떄
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            body_holder.tv_category.setSelected(true);
                            break;
                        //땟을때
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            body_holder.tv_category.setSelected(false);
                            break;
                    }
                    return false;
                }
            });


        }
    }

    private String getDiscount(String book_price, String book_sellprice) {
        Dlog.d(book_price + "/" + book_sellprice);
        double discount = 0;
        double after = Integer.parseInt(book_price);
        double before = Integer.parseInt(book_sellprice);

        discount = Math.round(((after - before) / after) * 100);

        return String.valueOf((int) discount);
    }

    @Override
    public int getItemCount() {
        return book_infos.size();
    }

    //바디
    public class ViewHolder_Body extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.book_title)
        TextView tv_title; //책제목
        @Nullable
        @BindView(R.id.book_publisher)
        TextView tv_publisher; //책지은이
        @Nullable
        @BindView(R.id.book_date_content)
        TextView tv_date; //출판년도
        @Nullable
        @BindView(R.id.book_category_content)
        TextView tv_category; //카테고리
        @Nullable
        @BindView(R.id.book_state_content)
        TextView tv_condition; //책 상태
        @Nullable
        @BindView(R.id.book_seller_content)
        TextView tv_nic; //판매자 이름
        @Nullable
        @BindView(R.id.book_price_before)
        TextView tv_before_price; //정가
        @Nullable
        @BindView(R.id.book_price_after)
        TextView tv_after_price; //판매가
        @Nullable
        @BindView(R.id.book_discount)
        TextView tv_discount; //할인률
        @Nullable
        @BindView(R.id.book_cover)
        ImageView iv_cover;
        @Nullable
        @BindView(R.id.item_click)
        MaterialRippleLayout btn_click;


        public ViewHolder_Body(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_click)
        public void itemClick(View v) {
            Intent intent = new Intent(context, Grouplist_detail_Activity.class);
            intent.putExtra("book_id", book_infos.get(getPosition()).getBook_id());
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
    }


}
