package com.jwoos.android.sellbook.intro;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.db.Preference;
import com.jwoos.android.sellbook.main.MainActivity;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.Set;

public class SplashActivity extends AppCompatActivity {

    private boolean is_login;
    private String book_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent extras = getIntent();
        book_id = extras.getStringExtra("book_id");

        Preference pref = new Preference(this);
        Set<String> preferences =  pref.getCookie();

        if (!preferences.isEmpty()) {
            is_login = true;
        } else {
            is_login = false;
        }

        PermissionSet();
    }

    private void PermissionSet() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(SplashActivity.this, "권한을 허용하셨습니다.", Toast.LENGTH_SHORT).show();
                Handler hd = new Handler();
                hd.postDelayed(new splashhandler(), 1500); // 3초 후에 hd Handler 실행
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(SplashActivity.this, "권한을 거부하셨습니다." + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(SplashActivity.this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }


        };


        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("셀북을 이용하기 위해서 권한이 필요합니다")
                //.setDeniedMessage("권한을 거부하셨습니다.\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                //.setGotoSettingButtonText("바로가기")
                .setPermissions(Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private class splashhandler implements Runnable {
        public void run() {

            Intent intent = null;
            //노티피케이션 & 자동로그인
            if (!ObjectUtils.isEmpty(book_id)) {
                Dlog.d("노티데이터 : " + book_id);
                intent = new Intent(SplashActivity.this, MainActivity.class);
                Gloval.setNoti_book_id(book_id);
            }
            if (is_login) {  //자동로그인
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }
    }
}
