package com.jwoos.android.sellbook.page1.grouplist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;

import butterknife.ButterKnife;


public class Grouplist_Fragment extends BaseFragment {
    public Grouplist_Fragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grouplist, container, false);
        ButterKnife.bind(this, rootView);


        return rootView;
    }










}
