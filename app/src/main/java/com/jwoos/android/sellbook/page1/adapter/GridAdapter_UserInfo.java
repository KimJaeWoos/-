package com.jwoos.android.sellbook.page1.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.myinfo.Myinfo_detail_Activity;
import com.jwoos.android.sellbook.page1.userinfo.Userinfo_Activity;
import com.jwoos.android.sellbook.widget.RialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jwoo on 2016-08-08.
 */
public class GridAdapter_UserInfo extends RecyclerView.Adapter<GridAdapter_UserInfo.ViewHolder> {

    private Userinfo_Activity activity;
    private List<Book_Info> book_infos;
    private boolean click_flag;
    private final static int REQ_CODE = 4321;

    //생성자
    public GridAdapter_UserInfo(Userinfo_Activity activity, List<Book_Info> list, boolean flag) {
        this.activity = activity;
        this.book_infos = list;
        this.click_flag = flag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //카드 아이템을 인플레이트
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_myinfo, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Picasso.with(activity)
                .load(book_infos.get(position).getBook_cover())
                .into(holder.iv_cover);
        holder.tv_name.setText(book_infos.get(position).getBook_name());
        holder.tv_price.setText(book_infos.get(position).getBook_price());

        //판매완료 유무
        if (book_infos.get(position).getSold_kind().equals("YES")) {
            holder.tv_soldout.setVisibility(View.VISIBLE);
        } else {
            holder.tv_soldout.setVisibility(View.GONE);
        }

        assert holder.btn_item != null;
        holder.btn_item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //누를떄,움직일떄
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        holder.tv_name.setSelected(true);
                        break;
                    //땟을때
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        holder.tv_name.setSelected(false);
                        break;
                }
                return false;
            }
        });

    }



    @Override
    public int getItemCount() {
        return book_infos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.image)
        ImageView iv_cover;
        @Nullable
        @BindView(R.id.book_name)
        TextView tv_name;
        @Nullable
        @BindView(R.id.book_price)
        RialTextView tv_price;
        @Nullable
        @BindView(R.id.soldout_view)
        TextView tv_soldout;
        @Nullable
        @BindView(R.id.card_view)
        CardView btn_item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        public void itemClick(View v) {
            //자신이 자신의 목록을볼떄 타인이 자신의 목록을볼때 구분
            if (click_flag) {
                Intent intent = null;
                intent = new Intent(activity, Myinfo_detail_Activity.class);
                intent.putExtra("book_id", book_infos.get(getPosition()).getBook_id());
                intent.putExtra("item_position", getPosition());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivityForResult(intent, REQ_CODE);
            } else {

            }
        }


    }
}
