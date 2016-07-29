package com.jwoos.android.sellbook.page1.grouplist;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;

import butterknife.OnClick;


public class PopupActivity extends BaseActivity {


    private String user_phone, book_name, book_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_custom2);
        initialize();

        Intent extras = getIntent();
        user_phone = extras.getStringExtra("phone");
        book_name = extras.getStringExtra("name");
        book_price = extras.getStringExtra("price");



    }

    private void initialize() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 70%
        //int height = (int) (display.getHeight() * 0.65);  //Display 사이즈의 90%
        //getWindow().getAttributes().width = width;
        //getWindow().getAttributes().height = height;
    }

    @OnClick({R.id.call, R.id.msg})
    public void click(View v) {
        Intent intent = null;
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP;

        switch (v.getId()) {
            case R.id.msg:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", user_phone);
                intent.putExtra("sms_body", "[구매요청]" + "\n" + "책 제목 : " + book_name + "\n" + "가격 : " + book_price + "\n");

                break;

            case R.id.call:
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user_phone));
                intent.setFlags(flags);
                break;
        }

        if (intent != null) {
            intent.setFlags(flags);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}
