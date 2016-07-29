package com.jwoos.android.sellbook.intro;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Login;
import com.jwoos.android.sellbook.utils.ObjectUtils;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jwoo on 2016-05-23.
 */
public class SignupActivity extends BaseActivity {


    @BindView(R.id.input_id)
    EditText et_id;
    @BindView(R.id.input_password)
    EditText et_pwd;
    @BindView(R.id.input_email)
    EditText et_email;
    @BindView(R.id.input_nickname)
    EditText et_nic;
    @BindView(R.id.input_major)
    Spinner sp_major;
    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.link_login)
    TextView btn_link_login;
    @BindView(R.id.chk_id)
    Button btn_id_chk;
    @BindView(R.id.chk_nic)
    Button btn_nik_chk;

    private int register_chk;
    private String user_nik;
    private boolean flag_nic, flag_id = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        register_chk = 0;

        String[] items = getResources().getStringArray(R.array.values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        sp_major.setAdapter(adapter);

    }

    @OnClick({R.id.chk_id, R.id.chk_nic, R.id.btn_signup, R.id.link_login})
    public void btn_click(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                signup();
                break;

            case R.id.link_login:
                finish();
                break;

            case R.id.chk_id:
                if (et_id.getText().toString().length() == 8 && !ObjectUtils.isEmpty(et_id.getText().toString())) {
                    et_id.setEnabled(false);
                    btn_id_chk.setEnabled(false);
                    ServiceGenerator.getService().Overlap_chk(et_id.getText().toString(), "1", new Callback<Login>() {
                        @Override
                        public void success(Login login, Response response) {
                            if (login.getStatus().equals("1")) {
                                showToast("가입가능한 학번입니다");
                                flag_id = true;
                            } else {
                                showToast("이미 가입되어있는 학번입니다");
                            }
                            et_id.setEnabled(true);
                            btn_id_chk.setEnabled(true);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            et_id.setEnabled(true);
                            btn_id_chk.setEnabled(true);
                            showToast("다시 시도해주세요");
                        }
                    });
                } else {
                    showToast("학번을 정확히 입력해주세요");
                }
                break;

            case R.id.chk_nic:
                if (!ObjectUtils.isEmpty(et_nic.getText().toString())) {
                    et_nic.setEnabled(false);
                    btn_nik_chk.setEnabled(false);
                    ServiceGenerator.getService().Overlap_chk(et_id.getText().toString(), "0", new Callback<Login>() {
                        @Override
                        public void success(Login login, Response response) {
                            if (login.getStatus().equals("1")) {
                                showToast("가입가능한 닉네임입니다");
                                flag_nic = true;
                            } else {
                                showToast("이미 사용중인 학번입니다");
                            }
                            et_nic.setEnabled(true);
                            btn_nik_chk.setEnabled(true);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            et_nic.setEnabled(true);
                            btn_nik_chk.setEnabled(true);
                            showToast("다시 시도해주세요");
                        }
                    });
                } else {
                    showToast("닉네임을 정확히 입력해주세요");
                }

                break;
        }
    }

    public void signup() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_nic.getWindowToken(), 0);
        if (!validate()) {
            showToast("회원가입 형식을 확인하세요 :)");
            return;
        } else if (!(flag_nic && flag_id)) {
            showToast("학번과 닉네임 중복체크를 해주세요 :)");
            return;
        }
        btn_signup.setEnabled(false);

        showDialog("아이디를 생성중입니다...");

        String id = et_id.getText().toString();
        String password = et_pwd.getText().toString();
        String email = et_email.getText().toString();
        user_nik = et_nic.getText().toString();
        String major = sp_major.getSelectedItem().toString();

        final Timer timer = new Timer();
        TimerTask upload_timer = new TimerTask() {
            public void run() {
                if (register_chk != 0) {
                    mHandler.obtainMessage(1).sendToTarget();
                    timer.cancel();
                }
            }
        };
        timer.schedule(upload_timer, 3000, 1000);

        //회원가입로직은 여기
        ServiceGenerator.getService().Signup(id, makeSha256Key(password), email, user_nik, major, new Callback<Login>() {
            @Override
            public void success(Login login, Response response) {
                if (login.getStatus().equals("1")) {
                    register_chk = 1;
                } else if (login.getStatus().equals("0")) {
                    register_chk = 2;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mHandler.obtainMessage(1).sendToTarget();
                timer.cancel();
            }
        });

    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (register_chk == 1) {  //통신완료 가입완료
                onSignupSuccess();
            } else if (register_chk == 2) {  //통신완료 가입실패
                onSignupFailed("다시시도해주세요 :)");
                dimssDialog();
                btn_signup.setEnabled(true);
            } else if (register_chk == 3) {  //통신이 실패하였을경우
                onSignupFailed("네트워크 환경에 문제가 발생하였습니다");
                dimssDialog();
                btn_signup.setEnabled(true);
            }
        }
    };

    public void onSignupSuccess() {
        Gloval.setUser_nik(user_nik);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(String msg) {
        showToast(msg);
    }

    public boolean validate() {
        boolean valid = true;

        String id = et_id.getText().toString();
        String password = et_pwd.getText().toString();
        String email = et_email.getText().toString();
        String nic = et_nic.getText().toString();
        String major = sp_major.getSelectedItem().toString();

        if (ObjectUtils.isEmpty(id) || id.length() != 8) {
            et_id.setError("학번 8자리를 입력해주세요");
            valid = false;
        } else {
            et_id.setError(null);
        }

        if (ObjectUtils.isEmpty(password) || password.length() < 4 ) {
            et_pwd.setError("비밀번호 4자리 이상 입력해주세요");
            valid = false;
        } else {
            et_pwd.setError(null);
        }

        if (ObjectUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("이메일 형식에 맞지않습니다");
            valid = false;
        } else {
            et_email.setError(null);
        }

        if (ObjectUtils.isEmpty(nic)) {
            et_nic.setError("닉네임을 입력해주세요");
            valid = false;
        } else {
            et_nic.setError(null);
        }

        if (major.isEmpty() && major.equals("소속대학을 선택하세요")) {
            showToast("소속대학을 선택해주세요");
            valid = false;
        } else {

        }

        return valid;
    }


    @Override
    protected void onDestroy() {
        dimssDialog();
        cancelToast();
        super.onDestroy();
    }
}
