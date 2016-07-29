package com.jwoos.android.sellbook.base;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.event_bus.BusProvider;

import dmax.dialog.SpotsDialog;


/**
 * Created by Jwoo on 2016-06-18.
 */
public abstract class BaseFragment extends Fragment {


    public String base_image_url_uploads = Gloval.getBase_image_url_uploads();
    public String base_image_url_profile = Gloval.getBase_image_url_profile();

    private AlertDialog dialog;
    private Toast toast;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void setToolbar(String title,Toolbar toolbar){
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitle(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = getFragmentManager().getBackStackEntryCount();

                if (count > 0) {
                    //stack에 1개이상 저장되어있을경우 진입
                    getFragmentManager().popBackStack();
                }else {
                    getActivity().finish();
                }
            }
        });
    }

    protected void showDialog(CharSequence msg){
        if (dialog != null && dialog.isShowing()) dimssDialog();
        dialog = new SpotsDialog(getContext(), msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void dimssDialog(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
    protected void showToast(String msg) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    protected int orientation(int orientation) {

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
