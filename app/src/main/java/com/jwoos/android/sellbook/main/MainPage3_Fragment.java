package com.jwoos.android.sellbook.main;

/**
 * Created by Jwoo on 2016-05-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.db.Preference;
import com.jwoos.android.sellbook.base.event_bus.ActivityResultEvent;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.intro.LoginActivity;
import com.jwoos.android.sellbook.page2.Page2_Activity;
import com.jwoos.android.sellbook.page3.Board_Activity;
import com.jwoos.android.sellbook.page3.Notice_Activity;
import com.jwoos.android.sellbook.page3.Qna_Activity;
import com.jwoos.android.sellbook.utils.Dlog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.Switch;
import com.squareup.otto.Subscribe;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainPage3_Fragment extends BaseFragment implements View.OnClickListener {
    public MainPage3_Fragment() {
    }

    private static final int INTENT_REQUEST_ACTIVITY_PHONE = 1004;

    @BindView(R.id.btn_setting_phone)
    Button btn_phone;
    @BindView(R.id.app_version)
    TextView tv_version;
    @BindView(R.id.btn_setting_push)
    Switch sc_push;

    private boolean push_flag;
    private Preference pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page3, container, false);
        ButterKnife.bind(this, rootView);
        pref = new Preference(getActivity());
        setPhoneNumber();
        setVeision();
        setPush();
        return rootView;
    }

    private void setPush() {
        push_flag = pref.getValue(true);
        Dlog.d(String.valueOf(push_flag));
        if (push_flag) {
            sc_push.setChecked(true);
        } else {
            sc_push.setChecked(false);
        }

    }

    private void setVeision() {
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String verSion = pInfo.versionName;
        tv_version.setText(verSion);
    }

    private void setPhoneNumber() {
        ServiceGenerator.getService().set_userPhone("1", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                btn_phone.setText(s);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @OnClick({R.id.btn_setting_notice, R.id.btn_setting_homepage, R.id.btn_setting_facebook,
            R.id.btn_setting_question, R.id.btn_setting_email, R.id.btn_setting_kakao,
            R.id.btn_setting_access, R.id.btn_setting_access2, R.id.btn_setting_opne,
            R.id.btn_setting_logout, R.id.btn_setting_push, R.id.btn_setting_phone, R.id.play_store})
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            //공지사항
            case R.id.btn_setting_notice: {
                intent = new Intent(getActivity(), Notice_Activity.class);
                break;
            }
            //푸시알림
            case R.id.btn_setting_push: {
                if (sc_push.isChecked()) {
                    pref.put(true);
                    Dlog.d("푸시활성화");
                } else {
                    pref.put(false);
                    Dlog.d("푸시비활성화");
                }
                break;
            }
            //홈페이지
            case R.id.btn_setting_homepage: {
                Gloval.setSetting_count(1);
                intent = new Intent(getActivity(), Board_Activity.class);
                break;
            }
            //페이스북
            case R.id.btn_setting_facebook: {
                showToast("준비중입니다 :)");
                break;
            }
            //자주묻는질문
            case R.id.btn_setting_question: {
                intent = new Intent(getActivity(), Qna_Activity.class);
                break;
            }
            //이메일문의
            case R.id.btn_setting_email: {
                Uri uri = Uri.parse("mailto:xxx@abc.com");
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                break;
            }
            //개인정보 취급방침
            case R.id.btn_setting_access: {
                showToast("준비중입니다 :)");
                break;
            }
            //평점남기기 스토어 연동
            case R.id.play_store: {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            }
            //서비스 이용약관
            case R.id.btn_setting_access2: {
                showToast("준비중입니다 :)");
                break;
            }
            //오픈소스 라이센스
            case R.id.btn_setting_opne: {
                Gloval.setSetting_count(5);
                intent = new Intent(getActivity(), Board_Activity.class);
                break;
            }
            case R.id.btn_setting_logout: {
                Dialog.Builder builder = null;
                builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        onClickLogout();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                String colorText = "<font color='black'>정말로 로그아웃을 하시겠습니까?<font>";
                ((SimpleDialog.Builder)builder).message(Html.fromHtml(colorText))
                        .positiveAction("확인")
                        .negativeAction("취소");

                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.setCancelable(false);
                fragment.show(getFragmentManager(), null);
                break;

            }
        }
        if (intent != null && v.getId() != R.id.play_store) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_setting_phone) {
            intent = new Intent(getActivity(), Page2_Activity.class);
            intent.putExtra("num", 5);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().startActivityForResult(intent, INTENT_REQUEST_ACTIVITY_PHONE);
        }


    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent activityResultEvent){
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
        //Log.d("이벤트버스", String.valueOf(activityResultEvent.getRequestCode())+"/"+activityResultEvent.getResultCode()+"/"+activityResultEvent.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_ACTIVITY_PHONE) {
                String phone = intent.getStringExtra("phone");
                ServiceGenerator.getService().set_userPhone(makePhoneNumber(phone), new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        btn_phone.setText(s);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }

    //딜레이 후 로그아웃
    private void onClickLogout() {
        ServiceGenerator.getService().delete_fcm(new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Dlog.d("토큰삭제");
                Preference pref = new Preference(getContext());
                pref.resetCookie();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    //전화번호 포멧변경
    public static String makePhoneNumber(String phoneNumber) {
        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
        if(!Pattern.matches(regEx, phoneNumber)) return null;
        return phoneNumber.replaceAll(regEx, "$1-$2-$3");
    }
}