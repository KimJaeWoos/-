package com.jwoos.android.sellbook.page3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.Gloval;

import butterknife.BindView;

/**
 * Created by Jwoo on 2016-07-01.
 */
public class Board_Activity extends BaseActivity {

    @BindView(R.id.webview)
    WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        int count = Gloval.getSetting_count();

        wb.getSettings().setJavaScriptEnabled(true);
        wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showDialog("로딩중입니다...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dimssDialog();
            }
        });

        if(count == 1) {//홈페이지
            setToolbar("셀북 홈페이지");
            wb.loadUrl("http://soo4131.cafe24.com");
        }
        else if(count == 2){//페이스북
            setToolbar("셀북 페이스북");
            wb.loadUrl("https://www.facebook.com/selphoneselphone");
        }
        else if(count == 3){//개인정보
            setToolbar("개인정보 취급방침");
            wb.loadUrl("http://soo4131.cafe24.com/setting/board.php");
        }
        else if(count == 4){//서비스
            setToolbar("서비스 이용약관");
            wb.loadUrl("http://soo4131.cafe24.com/setting/board2.php");
        }
        else if(count == 5){//오픈소스
            setToolbar("오픈소스 라이센스");
            wb.loadUrl("http://soo4131.cafe24.com/setting/opensource.php");
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
