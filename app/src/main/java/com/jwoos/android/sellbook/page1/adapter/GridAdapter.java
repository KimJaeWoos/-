package com.jwoos.android.sellbook.page1.adapter;

import android.app.Activity;
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
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.myinfo.Myinfo_detail_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    List<Book_Info> bookInfos;
    Activity mContext;
    private String base_image_url_uploads;
    private String base_image_url_profile;
    private boolean click_flag;
    private final static int REQ_CODE = 4321;

    public GridAdapter(Activity context, List<Book_Info> objects, boolean flag) {
        this.mContext = context;
        this.bookInfos = objects;
        this.click_flag = flag;
        this.base_image_url_uploads = Gloval.getBase_image_url_uploads();
        this.base_image_url_profile = Gloval.getBase_image_url_profile();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_myinfo, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {viewHolder.bind(i);}

    @Override
    public int getItemCount() {
        return bookInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /*@Nullable @BindView(R.id.card_view)
        CardView cardView;
        @Nullable @BindView(R.id.book_name)
        TextView tv_bookname;
        @Nullable @BindView(R.id.book_price)
        TextView tv_bookprice;
        @Nullable @BindView(R.id.book_content)
        TextView tv_bookcontent;
        @Nullable @BindView(R.id.image)
        ImageView imageView;
        @Nullable @BindView(R.id.soldout_view)
        TextView tv_soldout;*/

        private String image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(final int position){
            /*int book_rotate = Character.getNumericValue(bookInfos.get(position).getBook_image1().charAt(9));

            Picasso.with(mContext)
                    .load(base_image_url_uploads + bookInfos.get(position).getBook_image1())
                    .rotate(orientation(book_rotate))
                    .into(imageView);
            tv_bookname.setText(bookInfos.get(position).getBook_name());
            tv_bookprice.setText(bookInfos.get(position).getBook_price());
            tv_bookcontent.setText(bookInfos.get(position).getBook_content());
            image = bookInfos.get(position).getBook_image1();

            if(bookInfos.get(position).getSold_kind().equals("YES")){

                tv_soldout.setVisibility(View.VISIBLE);
            } else {
                tv_soldout.setVisibility(View.GONE);
            }

            if (click_flag) {
                assert cardView != null;
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        intent = new Intent(mContext,Myinfo_detail_Activity.class);
                        intent.putExtra("book_id", bookInfos.get(position).getBook_id());
                        intent.putExtra("item_position", position);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivityForResult(intent, REQ_CODE);
                        mContext.overridePendingTransition(0,0);
                    }
                });
            } else {

            }*/
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

    public void resetData(String kind, Boolean remove, int position) {
        int itemp_position = position + 1;
        Log.d("로그1", String.valueOf(position));
        Log.d("로그2", kind);
        Log.d("로그3", String.valueOf(remove));
        if (remove) {
            bookInfos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        } else {
            if (kind.equals("NO")) {
                bookInfos.get(position).setSold_kind("NO");
            } else {
                bookInfos.get(position).setSold_kind("YES");
            }
            notifyItemChanged(position);
        }
    }
}
