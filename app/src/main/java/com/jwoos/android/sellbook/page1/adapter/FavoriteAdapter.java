package com.jwoos.android.sellbook.page1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.retrofit.model.Favorites;
import com.jwoos.android.sellbook.page1.grouplist.Grouplist_detail_Activity;
import com.jwoos.android.sellbook.utils.TimeFomat;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Favorites> favorites;
    private Activity mContext;
    private String base_image_url_uploads;
    private String base_image_url_profile;
    private final static int REQ_FAV = 2500;

    public FavoriteAdapter(Context context, List<Favorites> objects) {
        this.mContext = (Activity)context;
        this.favorites = objects;
        this.base_image_url_uploads = Gloval.getBase_image_url_uploads();
        this.base_image_url_profile = Gloval.getBase_image_url_profile();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_favorite, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.card_view)
        CardView cardView;
        @Nullable @BindView(R.id.book_image)
        ImageView iv_book;
        @Nullable @BindView(R.id.book_name)
        TextView tv_bookname;
        @Nullable @BindView(R.id.time)
        TextView tv_time;
        @Nullable @BindView(R.id.book_price)
        TextView tv_bookprice;
        @Nullable @BindView(R.id.book_content)
        TextView tv_bookcontent;
        @Nullable @BindView(R.id.favorite_cnt)
        TextView tv_favorite;
        @Nullable @BindView(R.id.comment_cnt)
        TextView tv_comment;
        @Nullable @BindView(R.id.soldout_view)
        TextView tv_soldout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(int position) {
            //프로필 사진 회전값
            int image_rotate = Character.getNumericValue(favorites.get(position).getBook_image().charAt(9));
            Picasso.with(mContext)
                    .load(base_image_url_uploads + favorites.get(position).getBook_image())
                    .rotate(orientation(image_rotate))
                    .into(iv_book);
            tv_bookname.setText(favorites.get(position).getBook_name());
            tv_bookcontent.setText(favorites.get(position).getBook_content());
            tv_bookprice.setText(favorites.get(position).getBook_price());
            tv_favorite.setText(favorites.get(position).getFavorite_count());
            tv_comment.setText(favorites.get(position).getComment_count());


            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = format.parse(favorites.get(position).getBook_register_time());
                String time = TimeFomat.list_time(date);
                tv_time.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (favorites.get(position).getSold_kind().equals("YES")) {
                tv_soldout.setVisibility(View.VISIBLE);
            }

            click(favorites.get(position).getBook_id(), position);

        }

        private void click(final String book_id, final int position) {
            assert cardView != null;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Grouplist_detail_Activity.class);
                    intent.putExtra("book_id", book_id);
                    intent.putExtra("item_position", position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivityForResult(intent, REQ_FAV);
                    mContext.overridePendingTransition(0,0);
                }
            });
        }

    }
    //디테일에서 즐겨찾기 해제시 리스트 삭제
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_FAV) {
            if (resultCode == Activity.RESULT_OK) {
                int item_position = data.getIntExtra("item_position", 0);
                String item_flag = data.getStringExtra("item_flag");
                if (item_flag.equals("2")) {
                    favorites.remove(item_position);
                    notifyItemRemoved(item_position);
                    notifyItemRangeChanged(item_position, favorites.size());
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
