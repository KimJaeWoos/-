package com.jwoos.android.sellbook.page1.favorite;


import android.os.Bundle;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;

public class Favorite_Fragment_detailOne extends BaseActivity {

    /*@BindView(R.id.recycler_view)
    RecyclerView rv;

    private RecyclerView.LayoutManager mLayoutManager;
    private FavoriteAdapter adapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_group);
        setToolbar("즐겨찾기");

        /*rv.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        setAdapter();*/

    }

    /*private void setAdapter() {
        ServiceGenerator.getService().get_favorite_item(new Callback<List<Favorites>>() {
            @Override
            public void success(List<Favorites> favorites, Response response) {
                adapter = new FavoriteAdapter(Favorite_Activity.this, favorites);
                rv.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelToast();
    }
    @Override
    protected void onStop(){
        super.onStop();
        cancelToast();
    }*/
}

