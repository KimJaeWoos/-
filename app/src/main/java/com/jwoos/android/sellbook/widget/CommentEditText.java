package com.jwoos.android.sellbook.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Jwoo on 2016-07-22.
 */
public class CommentEditText extends EditText {

    Context context;
    private static LinearLayout btn_menu;
    private static LinearLayout footer;

    public CommentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            Log.d("커스텀","chk");
            //btn_menu.setVisibility(View.VISIBLE);
            //footer.setVisibility(View.INVISIBLE);
            InputMethodManager mgr = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);

        }
        return false;
    }
}
