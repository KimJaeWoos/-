package com.jwoos.android.sellbook.main;

/**
 * Created by Jwoo on 2016-05-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseFragment;
import com.jwoos.android.sellbook.base.event_bus.ActivityResultEvent;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.page2.BookInfo_Popup;
import com.jwoos.android.sellbook.page2.Page2_Activity;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.utils.ObjectUtils;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.coreorb.selectiondialogs.data.SelectableColor;
import pl.coreorb.selectiondialogs.data.SelectableIcon;
import pl.coreorb.selectiondialogs.dialogs.ColorSelectDialog;
import pl.coreorb.selectiondialogs.dialogs.IconSelectDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class MainPage2_Fragment extends BaseFragment implements ColorSelectDialog.OnColorSelectedListener {
    public MainPage2_Fragment() {
    }


    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_PAGE2_ACTIVITY_PRICE = 1002;
    private static final int INTENT_REQUEST_PAGE2_ACTIVITY_BOARD = 1003;
    private static final int INTENT_REQUEST_BOOKINFO_CEHCK = 9999;

    private static final String TAG = "TedPicker";
    private static final String TTB = "ttbwodn41312326001";
    ArrayList<Uri> image_uris = new ArrayList<Uri>();

    private boolean data_flag, image_flag;

    private ViewGroup mSelectedImagesContainer;
    private LinearLayout select1;

    private String data_image, data_condition, data_sellPrice, data_board = null;
    private String data_name, data_author, data_pubDate, data_description,
            data_cover, data_categoryName, data_publisher, data_price = null;
    private JSONArray aladin_json = null;

    private int retrofit_conunt;

    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.iv4)
    ImageView iv4;
    @BindView(R.id.iv5)
    ImageView iv5;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.btn_send)
    com.rey.material.widget.Button btn1;

    private String data_image1, data_image2, data_image3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page2, container, false);
        ButterKnife.bind(this, rootView);

        mSelectedImagesContainer = (ViewGroup) rootView.findViewById(R.id.selected_photos_container);
        select1 = (LinearLayout) rootView.findViewById(R.id.select2);

        return rootView;
    }


    @OnClick({R.id.select1, R.id.select2, R.id.select3, R.id.select4, R.id.select5, R.id.btn_send})
    public void select(View v) {

        Intent intent = null;
        switch (v.getId()) {
            //바코드스캔
            case R.id.select1:
                startScan();
                break;

            //사진추가
            case R.id.select2:
                Config config = new Config();
                config.setSelectionMin(1);
                config.setSelectionLimit(3);
                getImages(config);
                break;

            //책 등급 정하기
            case R.id.select3:
                showColorSelectDialog();
                break;

            //책 가격
            case R.id.select4:
                intent = new Intent(getActivity(), Page2_Activity.class);
                intent.putExtra("num", 3);
                intent.putExtra("input_data", data_sellPrice);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivityForResult(intent, INTENT_REQUEST_PAGE2_ACTIVITY_PRICE);
                break;

            //판매글 내용
            case R.id.select5:
                intent = new Intent(getActivity(), Page2_Activity.class);
                intent.putExtra("num", 4);
                intent.putExtra("input_data", data_board);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivityForResult(intent, INTENT_REQUEST_PAGE2_ACTIVITY_BOARD);
                break;

            case R.id.btn_send:
                ServiceGenerator.getService().set_userPhone("1", new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        if (s.equals("전화번호를 입력하세요")) {
                            Dialog.Builder builder = null;
                            builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                                @Override
                                public void onPositiveActionClicked(DialogFragment fragment) {
                                    Intent intent = new Intent(getActivity(), Page2_Activity.class);
                                    intent.putExtra("num", 5);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().startActivityForResult(intent, 1004);
                                    super.onPositiveActionClicked(fragment);
                                }

                                @Override
                                public void onNegativeActionClicked(DialogFragment fragment) {

                                    super.onNegativeActionClicked(fragment);
                                }
                            };

                            ((SimpleDialog.Builder) builder).message("전화번호를 등록해야합니다.\n지금 바로 등록하시겠습니까?")
                                    .title("확인")
                                    .positiveAction("확인")
                                    .negativeAction("취소");
                            DialogFragment fragment = DialogFragment.newInstance(builder);
                            fragment.show(getFragmentManager(), null);
                        } else {
                            Log.d("btn_send", btn1.getText().toString());
                            if (btn1.getText().toString().equals("등록하기")) {
                                showDialog("등록중입니다...");
                                final Timer timer = new Timer();
                                TimerTask upload_timer = new TimerTask() {
                                    public void run() {
                                        int limit = 0;
                                        if (retrofit_conunt == image_uris.size() + limit) {
                                            if (image_flag == true) {
                                                input_data(); //한번만 실행하도록
                                                limit++;
                                            } else {
                                                mHandler.obtainMessage(1).sendToTarget();
                                                timer.cancel();
                                            }
                                        }
                                        if (retrofit_conunt == (image_uris.size() + 1)) {
                                            mHandler.obtainMessage(1).sendToTarget();
                                            timer.cancel();
                                        }
                                    }
                                };
                                timer.schedule(upload_timer, 1000, 1000);
                                upload_file_rename();
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                break;

        }
        //getActivity().overridePendingTransition(0,0);

    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            chk_uploads();
        }
    };

    private void chk_uploads() {
        if (data_flag == true && image_flag == true) {
            data_reset();
            showToast("게시글 등록이 완료되었습니다 :)");
        } else {
            retrofit_conunt = 0;
            dimssDialog();
            data_flag = false;
            image_flag = false;
            showToast("등록이 실패되었습니다 다시 시도해주세요 :)");
        }
    }

    private void data_reset() {
        data_sellPrice = null;
        data_image = null;
        data_board = null;
        data_condition = null;
        data_name = null;
        image_uris.clear();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        check_input();
        dimssDialog();

        retrofit_conunt = 0;
        data_flag = false;
        image_flag = false;
    }

    private void upload_file_rename() {
        data_image1 = "";
        data_image2 = "";
        data_image3 = "";

        for (Uri uri : image_uris) {

            /**
             *  사진 정보 가저오기
             */
            //  이미지로 가져온다
            String imagePath = uri.getPath();

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            data_image = uri.toString();
            File file = new File(data_image);
            Date date = new Date();
            String register_time = String.valueOf(date.getTime());

            data_image = "_" + orientation + "_" + register_time + "_" + file.getName();

            if (data_image1.equals("")) {
                data_image1 = data_image;
            } else if (data_image2.equals("")) {
                data_image2 = data_image;
            } else if (data_image3.equals("")) {
                data_image3 = data_image;
            }
            uploadFile(file, data_image);
        }

    }

    private void input_data() {
        ServiceGenerator.getService().Create_board(data_name, data_author, data_pubDate, data_description, data_cover,
                data_categoryName, data_publisher, data_price, data_image1, data_image2, data_image3,
                data_condition, data_sellPrice, data_board, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        data_flag = true;
                        retrofit_conunt++;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        data_flag = false;
                        retrofit_conunt++;
                    }
                });

    }

    private void getImages(Config config) {
        ImagePickerActivity.setConfig(config);
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent activityResultEvent) {
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
        //Log.d("이벤트버스", String.valueOf(activityResultEvent.getRequestCode())+"/"+activityResultEvent.getResultCode()+"/"+activityResultEvent.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (image_uris.size() >= 1) {
                    showMedia();
                    select1.setVisibility(View.INVISIBLE);
                    data_image = image_uris.toString();

                } else {
                    mSelectedImagesContainer.setVisibility(View.GONE);
                    select1.setVisibility(View.VISIBLE);
                }
            }
            if (requestCode == INTENT_REQUEST_PAGE2_ACTIVITY_PRICE) {
                String price = intent.getStringExtra("price");
                data_sellPrice = price;
                iv4.setBackgroundResource(R.drawable.ic_action_done_red);
                tv4.setText(price);
            } else if (requestCode == INTENT_REQUEST_PAGE2_ACTIVITY_BOARD) {

                String board = intent.getStringExtra("board");
                data_board = board;
                iv5.setBackgroundResource(R.drawable.ic_action_done_red);
                tv5.setText(board);
            } else if (requestCode == INTENT_REQUEST_BOOKINFO_CEHCK) {
                iv1.setBackgroundResource(R.drawable.ic_action_done_red);
                tv1.setText(data_name);

            }
        }
        check_input();
    }

    private void check_input() {
        int btn_count = 5;
        if (data_board != null) {
            btn_count--;
        }
        if (data_condition != null) {
            btn_count--;
        }
        if (data_image != null) {
            btn_count--;
        }
        if (data_sellPrice != null) {
            btn_count--;
        }
        if (data_name != null) {
            btn_count--;
        }

        if (btn_count == 0) {
            btn1.setText("등록하기");
        } else {
            btn1.setText(btn_count + "개만 더 입력해주세요");
        }
    }

    private void showMedia() {

        mSelectedImagesContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());

        for (Uri uri : image_uris) {

            View imageHolder = LayoutInflater.from(getContext()).inflate(R.layout.page2_image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            Glide.with(this).load(uri.toString()).fitCenter().into(thumbnail);

            mSelectedImagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Config config = new Config();
                    config.setSelectionLimit(3);
                    getImages(config);
                }
            });
        }
    }


    //파일 업로드
    private void uploadFile(File file, String file_name) {


        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        ServiceGenerator.getService().upload(typedFile, file_name, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                image_flag = true;
                retrofit_conunt++;
            }

            @Override
            public void failure(RetrofitError error) {
                image_flag = false;
                retrofit_conunt++;
            }
        });
    }

    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("스캔중...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        //barcodeResult = barcode;
                        //result.setText(barcode.rawValue);
                        Dlog.d("스캔결과값 : " + barcode.rawValue);
                        getData("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbwodn41312326001&SearchTarget=book&output=js&Query=" + barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    InputStreamReader isr = new InputStreamReader(con.getInputStream());
                    BufferedReader br = new BufferedReader(isr, 1024);
                    String json;
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                showList(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    protected void showList(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            aladin_json = jsonObj.getJSONArray("item");
            Dlog.d(aladin_json.toString());

            for (int i = 0; i < aladin_json.length(); i++) {
                JSONObject c = aladin_json.getJSONObject(i);
                data_name = c.getString("title");   //책 타이틀
                data_author = c.getString("author");   //책 지은이
                data_pubDate = c.getString("pubDate");   //책 출시일
                data_description = c.getString("description");   //책 내용
                data_cover = c.getString("cover");   //책 커버사진
                data_categoryName = c.getString("categoryName");   //책 카테고리
                data_publisher = c.getString("publisher");   //책 출판사
                data_price = c.getString("priceStandard");
            }

            if (!ObjectUtils.isEmpty(data_name)) {
                Intent intent = new Intent(getContext(), BookInfo_Popup.class);
                intent.putExtra("cover", data_cover);
                intent.putExtra("name", data_name);
                intent.putExtra("author", data_author);
                intent.putExtra("publisher", data_publisher);
                getActivity().startActivityForResult(intent, INTENT_REQUEST_BOOKINFO_CEHCK);
            } else {
                showToast("지원하지 않는 책입니다.");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showColorSelectDialog() {
        new ColorSelectDialog.Builder(getContext())
                .setColors(R.array.ids,
                        R.array.names,
                        R.array.colors)
                .setTitle("책등급")
                .setSortColorsByName(false)
                .setOnColorSelectedListener(this)
                .build().show(getFragmentManager(), "TAG_SELECT_COLOR_DIALOG");
    }

    @Override
    public void onColorSelected(SelectableColor selectedItem) {
        String id = selectedItem.getId();
        data_condition = id;
        tv3.setText(data_condition);
        iv3.setBackgroundResource(R.drawable.ic_action_done_red);
        check_input();
    }

    /*public void showIconSelectDialog() {
        new IconSelectDialog.Builder(getContext())
                .setIcons(setIcons())
                .setTitle("책등급")
                .setSortIconsByName(false)
                .setOnIconSelectedListener(this)
                .build().show(getFragmentManager(), "");
    }*/

    /*@Override
    public void onIconSelected(SelectableIcon selectedItem) {
        String id = selectedItem.getId();
        data_condition = id;
        tv3.setText(data_condition);
        iv3.setBackgroundResource(R.drawable.ic_action_done_red);
        check_input();
    }*/

    /*private static ArrayList<SelectableIcon> setIcons() {
        ArrayList<SelectableIcon> selectionDialogsColors = new ArrayList<>();
        selectionDialogsColors.add(new SelectableIcon("A+", "뜯고 사용하지 않음, 필기가 전혀없음", R.drawable.garde_aplus));
        selectionDialogsColors.add(new SelectableIcon("A", "약간의 사용감 존재, 필기가 약간 있음", R.drawable.garde_a));
        selectionDialogsColors.add(new SelectableIcon("B", "사용흔적이 다수 있고, 대부분 페이지에 필기가 있음", R.drawable.garde_b));
        selectionDialogsColors.add(new SelectableIcon("C", "상태가 많이 안좋지만 보는데는 지장이 없음", R.drawable.garde_c));
        return selectionDialogsColors;
    }*/
}