package com.jwoos.android.sellbook.page1.grouplist;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;
import com.jwoos.android.sellbook.base.event_bus.ActivityResultEvent;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.page1.adapter.GroupAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

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
public class Grouplist_Fragment extends BaseFragment {
    public Grouplist_Fragment() {
    }


    private RecyclerView.LayoutManager mLayoutManager;
    private GroupAdapter mAdapter;

    private static final int MAX_ITEMS_PER_REQUEST = 20;
    private static final int SIMULATED_LOADING_TIME_IN_MS = 1500;
    private List<Book_Info> items;
    private boolean first_count;
    private int page;
    private int start_book_id;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.mySwipeRefreshLayout)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.search_empty)
    TextView tv_empty;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grouplist, container, false);
        ButterKnife.bind(this, rootView);
        initialize();
        getData(0, MAX_ITEMS_PER_REQUEST);
        SwipeRefresh();
        return rootView;
    }

    private void SwipeRefresh() {
        mySwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mySwipeRefreshLayout.setColorSchemeResources(
                android.R.color.black);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new GetDataTask().execute();
                        // 刷新动画开始后回调到此方法
                    }
                }

        );
    }


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
                page = 0;
                first_count = true;

            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mySwipeRefreshLayout.setRefreshing(false);
            mRecyclerView.removeAllViews();
            getData(0, MAX_ITEMS_PER_REQUEST);
            super.onPostExecute(result);
        }
    }


    public void initialize() {

        page = 0;
        mRecyclerView.removeAllViews();
        first_count = true;
        mAdapter = new GroupAdapter(getActivity(), items);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(MAX_ITEMS_PER_REQUEST, (LinearLayoutManager) mLayoutManager) {
            @Override
            public void onScrolledToEnd(final int firstVisibleItemPosition) {

                int start = ++page * MAX_ITEMS_PER_REQUEST; //(page x 30 )
                //Log.d("스크롤링", "start > item.size() " + start + ">" + items.size());
                final boolean allItemsLoaded = start > items.size();
                if (allItemsLoaded) {
                    //더 이상 데이터가 없음
                } else {
                    int start_id = start_book_id;
                    //Log.d("TAG", "스타트아이디" + start_id);
                    //int end = start + MAX_ITEMS_PER_REQUEST ;
                    getData(start_id, MAX_ITEMS_PER_REQUEST);
                    refreshView(mRecyclerView, new GroupAdapter(getActivity(), items), firstVisibleItemPosition);
                }
            }
        };
    }

    private void getData(int item_start, int item_end) {

        ServiceGenerator.getService().getBook_list(item_start, item_end, new Callback<List<Book_Info>>() {

            @Override
            public void success(final List<Book_Info> bookInfos, Response response) {
                if (bookInfos.get(0).getEmpty_chk().equals("true")) {
                    if (first_count == true) {
                        items = new ArrayList<>();
                        mAdapter = new GroupAdapter(getActivity(), bookInfos);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.addOnScrollListener(createInfiniteScrollListener());
                        first_count = false;

                        /*mRecyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(),Grouplist_detail_Activity.class);
                                        intent.putExtra("book_id", bookInfos.get(position).getBook_id());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(0,0);

                                    }
                                }));*/
                    }


                    //불러들인 데이터의 끝지점 book_id를 저장 이 번호 다음부터 데이터 요청

                    if (bookInfos.get(0).getEmpty_chk().equals("true")) {
                        start_book_id = Integer.parseInt(bookInfos.get(bookInfos.size() - 1).getBook_id());
                        for (int i = 0; i < bookInfos.size(); i++) {
                            items.add(bookInfos.get(i));

                        }
                        //마지막 리스폰서 개수와 MAX_ITEMS_PER_REQUEST 다를경우
                        if (bookInfos.size() != MAX_ITEMS_PER_REQUEST) {
                            page = bookInfos.size();
                        }
                    } else {
                        //마지막 리스폰서 개수와 MAX_ITEMS_PER_REQUEST 같을경우 다를경우보다 한번더 요청하고 Empty_chk를 체크하여 끝을 인지한다
                        page = items.size();
                    }

                } else { //게시글이 비었을때
                    tv_empty.setText("게시글이 없습니다.");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("TAG", error.getMessage());
            }
        });
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent activityResultEvent){
        mAdapter.onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
        //Log.d("이벤트버스", String.valueOf(activityResultEvent.getRequestCode())+"/"+activityResultEvent.getResultCode()+"/"+activityResultEvent.getData());
    }
}
