package com.jwoos.android.sellbook.page1.grouplist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.adapter.GroupAdapter;
import com.jwoos.android.sellbook.page1.adapter.RecyclerItemClickListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
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





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grouplist, container, false);
        ButterKnife.bind(this, rootView);




        //검색 숨기기, 타이틀바셋팅
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(search_keyword);


        return rootView;
    }



    //검색메뉴 숨기기
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

}