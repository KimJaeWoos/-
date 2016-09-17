package com.jwoos.android.sellbook.page2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BookInfo_Popup extends BaseActivity {


    @BindView(R.id.cover)
    ImageView iv_cover;
    @BindView(R.id.cover_back)
    ImageView iv_coverBack;
    @BindView(R.id.book_name)
    TextView tv_bookName;
    @BindView(R.id.book_publisher)
    TextView tv_bookPublisher;

    private String cover, name, author, publisher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_custom1);
        ButterKnife.bind(this);
        initialize();

        Intent extras = getIntent();
        cover = extras.getStringExtra("cover");
        name = extras.getStringExtra("name");
        author = extras.getStringExtra("author");
        publisher = extras.getStringExtra("publisher");


        tv_bookName.setText(name);
        tv_bookPublisher.setText(author + "|" + publisher);

        Picasso.with(getBaseContext())
                .load(cover)
                .into(iv_cover);
        tv_bookName.setSelected(true);
        tv_bookPublisher.setSelected(true);

    }

    private void initialize() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 70%
        //int height = (int) (display.getHeight() * 0.65);  //Display 사이즈의 90%
        //getWindow().getAttributes().width = width;
        //getWindow().getAttributes().height = height;
    }

    @OnClick(R.id.msg)
    public void click(View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
