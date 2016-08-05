package com.jwoos.android.sellbook.base.call;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Book_Info;
import com.jwoos.android.sellbook.base.retrofit.model.Calling;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.utils.ImageRotate;
import com.jwoos.android.sellbook.utils.TimeFomat;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.jwoos.android.sellbook.widget.RialTextView;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CallingService extends Service {

    public static final String EXTRA_CALL_NUMBER = "call_number";
    protected View rootView;


    @BindView(R.id.pop_profile)
    MLRoundedImageView iv_profile;
    @BindView(R.id.pop_phone)
    TextView tv_phone;
    @BindView(R.id.pop_nic)
    TextView tv_nic;
    @BindView(R.id.pop_time)
    TextView tv_time;
    @BindView(R.id.pop_name)
    TextView tv_name;
    @BindView(R.id.pop_price)
    RialTextView tv_price;
    @BindView(R.id.pop_btn)
    Button btn_detail;

    String call_number;
    String base_image_url_profile;

    WindowManager.LayoutParams params;
    private WindowManager windowManager;


    @Override
    public IBinder onBind(Intent intent) {

        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%


        params = new WindowManager.LayoutParams(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.call_popup_top, null);
        ButterKnife.bind(this, rootView);
        setDraggable();


    }



    private void setDraggable() {

        rootView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        if (rootView != null)
                            windowManager.updateViewLayout(rootView, params);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        setExtra(intent);

        if (!TextUtils.isEmpty(call_number)) {
            Dlog.d(call_number);
            ServiceGenerator.getService().get_record(call_number, new Callback<List<Calling>>() {

                @Override
                public void success(List<Calling> callings, Response response) {
                    if (!callings.get(0).getPop_nic().equals("empty")) {
                        Dlog.d("성공");
                        windowManager.addView(rootView, params);
                        int book_rotate = Character.getNumericValue(callings.get(0).getPop_profile().charAt(9));

                        Picasso.with(getBaseContext())
                                .load(base_image_url_profile + callings.get(0).getPop_profile())
                                .rotate(ImageRotate.orientation(book_rotate))
                                .into(iv_profile);

                        tv_phone.setText(call_number);
                        tv_nic.setText(callings.get(0).getPop_nic());
                        tv_name.setText(callings.get(0).getPop_name());
                        tv_price.setText(callings.get(0).getPop_price());

                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = null;
                        try {
                            date = format.parse(callings.get(0).getPop_time());
                            String time = TimeFomat.list_time(date);
                            tv_time.setText(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Dlog.e("실패");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Dlog.e(error.getMessage());
                }
            });
        }



        return START_REDELIVER_INTENT;
    }


    private void setExtra(Intent intent) {

        if (intent == null) {
           // removePopup();
            return;
        }

        call_number = intent.getStringExtra(EXTRA_CALL_NUMBER);
        base_image_url_profile = Gloval.getBase_image_url_profile();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removePopup();
    }

    @OnClick({R.id.pop_close})
    public void removePopup() {
        if (rootView != null && windowManager != null) windowManager.removeView(rootView);
    }

}
