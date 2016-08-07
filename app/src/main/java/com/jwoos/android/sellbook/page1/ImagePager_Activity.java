package com.jwoos.android.sellbook.page1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.widget.CustomViewPager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Jwoo on 2016-06-20.
 */
public class ImagePager_Activity extends BaseActivity {


    private static String base_url = Gloval.getBase_image_url_uploads();
    private String book_img1, book_img2, book_img3, book_img4, book_img5;
    private static String[] images;

    private ViewPager mViewPager;

    @BindView(R.id.tv_count)
    TextView tv_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepager);
        Bundle bundle = getIntent().getExtras();
        book_img1 = bundle.getString("image1");
        book_img2 = bundle.getString("image2");
        book_img3 = bundle.getString("image3");
        book_img4 = bundle.getString("image4");
        book_img5 = bundle.getString("image5");
        setImage_url();

        tv_count.setText((1+"/"+images.length));

        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new PhotoPagerAdapter(this));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_count.setText((position+1+"/"+images.length));
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private void setImage_url() {
        if (book_img2.length() <1) {
            images = new String[]{book_img1};
        } else if (book_img3.length() < 1) {
            images = new String[]{book_img1,book_img2};
        } else if (book_img4.length() < 1) {
            images = new String[]{book_img1,book_img2, book_img3};
        } else if (book_img5.length() < 1) {
            images = new String[]{book_img1,book_img2,book_img4};
        } else {
            images = new String[]{book_img1,book_img2,book_img3,book_img4,book_img5};
        }
    }

    @OnClick({R.id.iv_exit})
    public void click(View v){
        finish();
    }


    static class PhotoPagerAdapter extends PagerAdapter {

        Context mcontext;

        public PhotoPagerAdapter(ImagePager_Activity mcontext) {
           this.mcontext = mcontext;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            int book_rotate = Character.getNumericValue(images[position].charAt(9));

            Picasso.with(mcontext)
                    .load(base_url+images[position])
                    .rotate(orientation(book_rotate))
                    .into(photoView);
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private int orientation(int orientation) {

            int rotate = 0;

            if (orientation == 2)
                rotate = 0;
            else if (orientation == 3)
                rotate = 180;
            else if (orientation == 4)
                rotate = 180;
            else if (orientation == 5)
                rotate = 90;
            else if (orientation == 6)
                rotate = 90;
            else if (orientation == 7)
                rotate = -90;
            else if (orientation == 8)
                rotate = -90;

            return rotate;
        }
    }
}
