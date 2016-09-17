package com.jwoos.android.sellbook.page1.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.adapter.GridAdapter_Favorite;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.utils.ObjectUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Favorite_Fragment extends BaseFragment {
    public Favorite_Fragment() {
    }

    @BindView(R.id.favorite_one)
    RecyclerView rv_one;
    @BindView(R.id.favorite_two)
    RecyclerView rv_two;
    @BindView(R.id.favorite_one_empty)
    LinearLayout li_one;
    @BindView(R.id.favorite_two_empty)
    LinearLayout li_two;

    private GridAdapter_Favorite adpater_one;
    private GridAdapter_Favorite adpater_two;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favorite_group, container, false);
        ButterKnife.bind(this, rootView);
        Dlog.d("생명주기");
        initialize_one();
        initialize_two();

        return rootView;
    }


    private void initialize_one() {
        ServiceGenerator.getService().get_favorite_item(new Callback<List<Book_Info>>() {
            @Override
            public void success(List<Book_Info> favorites, Response response) {
                if (!ObjectUtils.isEmpty(favorites)) {
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 3);
                    rv_one.setLayoutManager(lLayout);
                    rv_one.setHasFixedSize(true);
                    adpater_one = new GridAdapter_Favorite(getActivity(), favorites);
                    rv_one.setAdapter(adpater_one);
                } else {
                    adpater_one = new GridAdapter_Favorite(getActivity(), favorites);
                    li_one.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Dlog.e(error.getMessage());
            }
        });
    }

    private void initialize_two() {
        ServiceGenerator.getService().get_histoty(new Callback<List<Book_Info>>() {
            @Override
            public void success(List<Book_Info> book_infos, Response response) {
                if (!ObjectUtils.isEmpty(book_infos)) {
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 3);
                    rv_two.setLayoutManager(lLayout);
                    rv_two.setHasFixedSize(true);
                    adpater_two = new GridAdapter_Favorite(getActivity(), book_infos);
                    rv_two.setAdapter(adpater_two);
                } else {
                    li_two.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Dlog.e(error.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Dlog.d("생명주기");
    }

    @Override
    public void onPause() {
        super.onPause();
        Dlog.d("생명주기");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dlog.d("생명주기");
    }

    @Override
    public void onResume() {
        super.onResume();
        Dlog.d("생명주기");
    }

    @Override
    public void onStart() {
        super.onStart();
        Dlog.d("생명주기");
    }
}
