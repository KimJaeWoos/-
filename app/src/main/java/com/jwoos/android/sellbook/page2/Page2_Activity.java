package com.jwoos.android.sellbook.page2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.utils.ObjectUtils;

/**
 * Created by Jwoo on 2016-05-24.
 */
public class Page2_Activity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent extras = getIntent();
        int num = extras.getIntExtra("num",0);
        String input_data = extras.getStringExtra("input_data");
        switch (num){
            case 3:
                input_price(input_data);
                break;
            case 4:
                input_board(input_data);
                break;
            case 5:
                input_phone();
                break;
        }

    }

    private void input_price(String input_data){
        setContentView(R.layout.activity_input_price);
        setToolbar("판매가격 정하기");
        final EditText tv = (EditText)findViewById(R.id.input_price);
        final ImageButton btn2 = (ImageButton)findViewById(R.id.setting_check);
        final Button btn3 = (Button)findViewById(R.id.setting_check2);

        tv.setText(input_data);
        tv.setSelection(tv.length());

        assert btn2 != null; //설정값들이 일치하는지 검사 ??
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = tv.getText().toString();
                int rest = 0;
                if (!ObjectUtils.isEmpty(price)) {
                    rest = Integer.parseInt(price)%1000;
                }
                if (ObjectUtils.isEmpty(price)){
                    showToast("금액을 입력해주세요 :)");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                } else if (rest != 0) {
                    showToast("1000원 단위로만 입력가능합니다");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                } else {
                    if (Integer.parseInt(price) == 0) {
                        price = "무료나눔";
                    }
                    Bundle extra = new Bundle();
                    Intent intent = new Intent();
                    extra.putString("price",price);
                    intent.putExtras(extra);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }

            }
        });

        assert btn3 != null;
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = tv.getText().toString();
                int rest = 0;
                if (!ObjectUtils.isEmpty(price)) {
                    rest = Integer.parseInt(price)%1000;
                }
                if (ObjectUtils.isEmpty(price)){
                    showToast("금액을 입력해주세요 :)");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                } else if (rest != 0) {
                    showToast("1000원 단위로만 입력가능합니다");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                } else {
                    if (Integer.parseInt(price) == 0) {
                        price = "무료나눔";
                    }
                    Bundle extra = new Bundle();
                    Intent intent = new Intent();
                    extra.putString("price",price);
                    intent.putExtras(extra);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void input_board(String input_data){
        setContentView(R.layout.activity_input_board);
        setToolbar("판매글 작성하기");

        final EditText tv = (EditText)findViewById(R.id.input_board);
        final ImageButton btn2 = (ImageButton)findViewById(R.id.setting_check);
        final Button btn3 = (Button)findViewById(R.id.setting_check2);

        tv.setText(input_data);
        tv.setSelection(tv.length());

        assert btn2 != null; //설정값들이 일치하는지 검사 ??
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tv.getText().toString();
                if (ObjectUtils.isEmpty(text)){
                    showToast("내용을 입력해주세요 :)");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                }else {
                    Bundle extra = new Bundle();
                    Intent intent = new Intent();
                    extra.putString("board",text);
                    intent.putExtras(extra);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });

        assert btn3 != null;
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tv.getText().toString();
                if (ObjectUtils.isEmpty(text)){
                    showToast("내용을 입력해주세요 :)");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                }else {
                    Bundle extra = new Bundle();
                    Intent intent = new Intent();
                    extra.putString("board",text);
                    intent.putExtras(extra);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void input_phone(){
        setContentView(R.layout.activity_input_phone);
        setToolbar("연락처 작성하기");

        final EditText tv = (EditText)findViewById(R.id.input_phone);
        final ImageButton btn2 = (ImageButton)findViewById(R.id.setting_check);
        final Button btn3 = (Button)findViewById(R.id.setting_check2);

        assert btn2 != null; //설정값들이 일치하는지 검사 ??
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = tv.getText().toString();
                int phone_len = phone.length();

                if (phone_len != 11 || ObjectUtils.isEmpty(phone)){
                    showToast("연락처를 정확히 입력해주세요 :)");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                }else {
                    Bundle extra = new Bundle();
                    Intent intent = new Intent();
                    extra.putString("phone",phone);
                    intent.putExtras(extra);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }

            }
        });

        assert btn3 != null;
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = tv.getText().toString();
                int phone_len = phone.length();

                if (phone_len != 11 || ObjectUtils.isEmpty(phone)){
                    showToast("연락처를 정확히 입력해주세요 :)");
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(tv);
                }else {
                    Bundle extra = new Bundle();
                    Intent intent = new Intent();
                    extra.putString("phone",phone);
                    intent.putExtras(extra);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
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
    }

}
