package com.jwoos.android.sellbook.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.event_bus.ActivityResultEvent;
import com.jwoos.android.sellbook.base.event_bus.BusProvider;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


/**
 * Created by Jwoo on 2016-06-18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private Toolbar toolbar;
    private static Toast toast;

    public String base_image_url_uploads = Gloval.getBase_image_url_uploads();
    public String base_image_url_profile = Gloval.getBase_image_url_profile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected void showDialog(String msg){
        if (dialog != null && dialog.isShowing()) dimssDialog();
        dialog =new SpotsDialog(this, msg);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    protected void showToast(String msg) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void cancelToast(){
        if (toast != null)
            toast.cancel();
    }


    protected void setToolbar(String title){
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    protected static String makeSha256Key(String str){
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return str+"-"+System.currentTimeMillis();
        }
        try {
            sha256.update(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
        }
        //sha256.update(str.getBytes());
        byte[] bytes = sha256.digest();
        int bytes_length = bytes.length;
        StringBuffer strBuffer = new StringBuffer();
        for(int i = 0 ; i < bytes_length; i++){
            strBuffer.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //System.out.println(strBuffer.toString().toUpperCase());
        return strBuffer.toString().toUpperCase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }
}
