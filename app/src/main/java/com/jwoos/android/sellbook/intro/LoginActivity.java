package com.jwoos.android.sellbook.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BackPressCloseHandler;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Login;
import com.jwoos.android.sellbook.main.MainActivity;
import com.jwoos.android.sellbook.utils.ObjectUtils;

import butterknife.BindView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends BaseActivity{

    private static final String TAG = "LoginActiviy";
    private static final int REQUEST_SIGNUP = 0;
    private BackPressCloseHandler backPressCloseHandler;

    @BindView(R.id.input_id)
    EditText et_id;
    @BindView(R.id.input_password)
    EditText et_pwd;
    @BindView(R.id.btn_login)
    com.rey.material.widget.Button btn_login;
    @BindView(R.id.link_signup)
    TextView btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backPressCloseHandler = new BackPressCloseHandler(this);

        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login(){
        if(!validate()){
            onLoginFailed();
            return;
        }
        showDialog("로그인중입니다...");
        set_Enabled(false);

        String id = et_id.getText().toString();
        String password = et_pwd.getText().toString();

        ServiceGenerator.getService().Login(id, makeSha256Key(password), new Callback<Login>() {
            @Override
            public void success(Login login, Response response) {

                if(login.getStatus().equals("1")) {
                    set_Enabled(true);
                    onLoginSuccess();
                    Gloval.setUser_nik(login.getUser_nic());
                    //dimssDialog();
                } else {
                    set_Enabled(true);
                    dimssDialog();
                    showToast("아이디와 패스워드가 일치하지 않습니다");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.getMessage();
                set_Enabled(true);
                dimssDialog();
                showToast("다시시도해주세요 :)");
            }
        });

    }

    private void set_Enabled(Boolean enabled) {
        btn_login.setEnabled(enabled);
        et_id.setEnabled(enabled);
        et_pwd.setEnabled(enabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SIGNUP){
            if(resultCode == RESULT_OK){
                //회원가입이 성공적일때 로직
                showDialog("로그인중입니다...");
                onLoginSuccess();
            }
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0,0);
        btn_login.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        showToast("로그인에 실패하였습니다");
        btn_login.setEnabled(true);
    }

    public boolean validate(){
        boolean valid = true;

        String id = et_id.getText().toString();
        String password = et_pwd.getText().toString();

        if(ObjectUtils.isEmpty(id) || id.length() != 8){
            et_id.setError("학번 8자리를 정확히 입력해주세요");
            valid = false;
        } else {
            et_id.setError(null);
        }

        if(ObjectUtils.isEmpty(password) || password.length() < 4){
            et_pwd.setError("패스워드를 정확히 입력해주세요");
            valid = false;
        }else {
            et_pwd.setError(null);
        }

        return valid;
    }


    @Override
    protected void onDestroy() {
        dimssDialog();
        cancelToast();
        super.onDestroy();
    }
    @Override
    protected void onStop(){
        super.onStop();
        cancelToast();
    }
}
