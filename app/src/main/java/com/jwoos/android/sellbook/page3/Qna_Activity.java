package com.jwoos.android.sellbook.page3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Notice;

import net.cachapa.expandablelayout.ExpandableLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jwoo on 2016-07-05.
 */
public class Qna_Activity extends BaseActivity {


    @BindView(R.id.recyclerView_qna)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        showDialog("로딩중입니다...");
        setToolbar("자주묻는질문");

        ServiceGenerator.getService().getQna(new Callback<List<Notice>>() {
            @Override
            public void success(List<Notice> qna_models, Response response) {

                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setAdapter(new SimpleAdapter(qna_models));
                dimssDialog();

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }

    public static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

        static List<Notice> qna_list;

        public SimpleAdapter(List<Notice> objects ) {
            this.qna_list = objects;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_item, parent, false);


            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return qna_list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {

            @Nullable
            @BindView(R.id.expandable_layout)
            ExpandableLinearLayout expandableLayout;
            @Nullable @BindView(R.id.expand_button)
            TextView expandButton;
            @Nullable @BindView(R.id.expand_button2)
            TextView expandButton2;



            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                //expandButton.setBackgroundColor(Color.WHITE);
                expandButton.setOnClickListener(this);
                expandButton.setFocusableInTouchMode(true);
                expandButton.setOnFocusChangeListener(this);
            }

            public void bind(int position) {
                expandButton.setText(qna_list.get(position).getQna_name());
                expandButton2.setText(qna_list.get(position).getQna_content());
            }

            @Override
            public void onClick(View view) {
                if (expandButton.isFocused()) {
                    expandButton.clearFocus();
                } else {
                    expandButton.requestFocus();
                }
            }

            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) {
                    expandableLayout.expand();
                } else {
                    expandableLayout.collapse();
                }
            }
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}