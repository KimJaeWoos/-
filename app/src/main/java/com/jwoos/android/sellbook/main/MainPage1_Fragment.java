package com.jwoos.android.sellbook.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;
import com.jwoos.android.sellbook.page1.favorite.Favorite_Activity;
import com.jwoos.android.sellbook.page1.grouplist.Grouplist_Activity;
import com.jwoos.android.sellbook.page1.myinfo.Myinfo_Activity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jwoo on 2016-05-23.
 */
public class MainPage1_Fragment extends BaseFragment {
    public MainPage1_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        ButterKnife.bind(this, rootView);
        /*
        progress = new Dialog(getActivity(), R.style.progress_dialog);
        progress.setContentView(R.layout.layout_dialog_loading);
        progress.setCancelable(false);
        progress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progress.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("로딩중입니다...");
        progress.show();*/
        return rootView;
    }

    @OnClick({R.id.btn_search, R.id.btn_search2, R.id.btn_search3})
    public void search_click(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_search:
                intent = new Intent(getActivity(), Grouplist_Activity.class);
                break;

            case R.id.btn_search2:
                intent = new Intent(getActivity(), Myinfo_Activity.class);
                break;

            case R.id.btn_search3:
                intent = new Intent(getActivity(), Favorite_Activity.class);
                break;
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}
