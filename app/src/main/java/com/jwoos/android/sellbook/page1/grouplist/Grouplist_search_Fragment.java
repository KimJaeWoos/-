package com.jwoos.android.sellbook.page1.grouplist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.adapter.GroupAdapter;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.utils.ObjectUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jwoo on 2016-05-31.
 */
public class Grouplist_search_Fragment extends BaseFragment {
    public Grouplist_search_Fragment() {
    }

    private String search_keyword;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.result_header)
    TextView tv;
    @BindView(R.id.searh_empty)
    RelativeLayout empty_msg;
    @BindView(R.id.search_ment)
    TextView tv_msg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, rootView);
        initialize();


        return rootView;
    }

    private void initialize() {
        search_keyword = getArguments().getString("keyword");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(search_keyword);
        ServiceGenerator.getService().getBook_search(search_keyword, new Callback<List<Book_Info>>() {
            @Override
            public void success(List<Book_Info> book_infos, Response response) {
                if (!book_infos.get(0).getEmpty_chk().equals("empty")) {
                    tv.setText("총 " + book_infos.size() +"건 검색");
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(layoutManager);
                    GroupAdapter rvAdapter = new GroupAdapter(getActivity(), book_infos);
                    rv.setAdapter(rvAdapter);
                } else {
                    tv.setVisibility(View.GONE);
                    empty_msg.setVisibility(View.VISIBLE);
                    tv_msg.setText("'" + search_keyword + "'" + "검색결과가 없습니다");
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Dlog.e(error.getMessage());
            }
        });
    }




    //검색메뉴 숨기기
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

}